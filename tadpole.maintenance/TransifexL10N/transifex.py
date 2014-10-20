# coding=utf-8
__author__ = 'jangbi882@gmail.com'

########################################################
# Script for TadpoleUI trans by http://www.Transifex.com

import sys
import os
import re
import shutil


PROJECT_NAME = "tadpoleui"
SRC_DIR = "../../"
DST_DIR = "" #"c:/OpenGIS/TadpoleDbHub/TransifexL10N"

PATH_PATTERN1 = "/?.*/src/"
RES_PATTERN1 = "/src/(.*)/(messages).*\\.properties"
PATH_PATTERN2 = "/OSGI-INF/l10n"
RES_PATTERN2 = "/(\\w+\\.[\\w\\.]+)/.*/(bundle).*\\.properties"

LANG_PATTERN = "_(.*)\\.properties"
DEFAULT_LANG = "en"
LANG_LIST = ["ko", "ep", "id"] # 새로운 언어기 추가되면 이 리스트에 추가

# 도움말
def show_help(cmd):
    print("""
Usage
=====
python {0} -update: push source and pull translated resources
python {0} -init: Initialize configuration for Transifex Client(tx)
python {0} -push_source: Push source resource ({1}) to transifex.com
python {0} -push_trans: Push translated resource to transifex.com
python {0} -pull_trans: Pull translated resource from transifex.com
python {0} -help: Show this message
""".format(cmd, DEFAULT_LANG))


# tx 설치확인
def checkTx():
    rc = os.system("tx --version")
    if rc == 1:
        print (
"""
[ERROR] Transifex Client, tx command is not installed.
        Please setup Transifex Client first.
        Refer to the following link.
        http://docs.transifex.com/developer/client/setup
"""
        )
        return False
    return True


# tx init 확인
def checkTxInit():
    pwd = os.getcwd()
    configFolder = os.path.join(pwd, ".tx")
    #print configFolder
    if os.path.isdir(configFolder):
        return True
    print (
"""
[ERROR] Cannot find any .tx directory!
        Run 'tx init' to initialize your project first!
        ProjectNotInit: Cannot find any .tx directory!
"""
    )
    return False


# convert package name to resource name
def getResName(path):
    path = path.replace("\\", "/")
    if re.search(PATH_PATTERN1, path):
        matched = re.findall(RES_PATTERN1, path)
        if matched:
            return matched[0][0].replace("/", "_").replace("com_hangum_tadpole_", "").replace("com_hamgum_tadpole_", "")+"__"+matched[0][1]
    elif re.search(PATH_PATTERN2, path):
        matched = re.findall(RES_PATTERN2, path)
        if matched:
            return matched[0][0].replace(".", "_").replace("com_hangum_tadpole_", "").replace("com_hamgum_tadpole_", "")+"__"+matched[0][1]

    return None

def checkFolderPattern(path):
    path = path.replace("\\", "/")
    if re.search(PATH_PATTERN1, path) or re.search(PATH_PATTERN2, path):
        return True
    return False

# 파일명에서 언어 추출
def getLang(filename):
    langs = re.findall(LANG_PATTERN, filename)
    if langs:
        lang = langs[0]
    else:
        lang = DEFAULT_LANG
    return lang


### Transifex 설정파일 초기화
#  [주의] 원본 리소스는 Transifex.com에 미리 등록되어 있어야 한다.
def init_resource():
    ## 하위 모든 폴더 탐색 시작
    for (path, dir, files) in os.walk(SRC_DIR):
        # 패턴과 일치 하지 않는 폴더 스킵
        if not checkFolderPattern(path):
            continue

        # 폴더 내 파일들에 대해서
        for filename in files:
            src_path = os.path.join(path, filename)

            # make resource name from path
            resource_name = getResName(src_path)
            if not resource_name:
                continue

            # 언어 확인
            lang = getLang(filename)

            # 설정파일 만들기
            if lang == DEFAULT_LANG:
                dst_path = os.path.join(DST_DIR, "{0}.properties".format(resource_name))
                # 원본 파일 설정 생성
                cmd = "tx set -t PROPERTIES --source -r {0}.{1} -l en {1}.properties".format(PROJECT_NAME, resource_name)
            else:
                dst_path = os.path.join(DST_DIR, "{0}_{1}.properties".format(resource_name, lang))
                # 번역 파일 설정 생성
                cmd = "tx set -r {2}.{0} -l {1} {0}_{1}.properties".format(resource_name, lang, PROJECT_NAME)

            # 파일이 이미 있는지 확인
            flag_need_temp = True
            if os.path.isfile(dst_path):
                flag_need_temp = False

            # 없다면 임시파일 만들기
            if flag_need_temp:
                temp_file = open(dst_path, 'w')
                temp_file.close()

            # 등록 명령 실행
            print cmd
            rc = os.system(cmd)

            # 임시파일 만들었다면 지우기
            if flag_need_temp:
                os.remove(dst_path)
    ## 하위폴더 탐색 끝


## 레파지토리에서 복사해 와 Transifex.com에 원본 올리기
def push_source():
    ## 하위 모든 폴더 탐색 시작
    for (path, dir, files) in os.walk(SRC_DIR):
        # 패턴과 일치 하지 않는 폴더 스킵
        if not checkFolderPattern(path):
            continue

        # 폴더 내 파일들에 대해서
        for filename in files:
            src_path = os.path.join(path, filename)

            # make resource name from path
            resource_name = getResName(src_path)
            if not resource_name:
                continue

            # 언어 추출
            lang = getLang(filename)
            if lang != DEFAULT_LANG:
                continue

            dst_path = os.path.join(DST_DIR, "{0}.properties".format(resource_name))

            # 파일 복사
            shutil.copy2(src_path, dst_path)

            cmd = "tx push -r {0}.{1} -s -f --no-interactive".format(PROJECT_NAME, resource_name)
            print cmd
            rc = os.system(cmd)
            if rc != 0:
                print "[WARNING] Fail to register {0}".format(dst_path)
    ## 하위폴더 탐색 끝


## Transifex.com에 로컬의 번역 올리기
def push_trans():
    ## 하위 모든 폴더 탐색 시작
    for (path, dir, files) in os.walk(SRC_DIR):
        # 패턴과 일치 하지 않는 폴더 스킵
        if not checkFolderPattern(path):
            continue

        # 폴더 내 파일들에 대해서
        for filename in files:
            src_path = os.path.join(path, filename)

            # make resource name from path
            resource_name = getResName(src_path)
            if not resource_name:
                continue

            # 언어 추출
            lang = getLang(filename)
            if lang == DEFAULT_LANG:
                continue

            dst_path = os.path.join(DST_DIR, "{0}_{1}.properties".format(resource_name, lang))

            # 파일 복사
            shutil.copy2(src_path, dst_path)

            cmd = "tx push -l {1} -r {2}.{0} -t -f --no-interactive".format(resource_name, lang, PROJECT_NAME)
            print cmd
            rc = os.system(cmd)
            if rc != 0:
                print "[WARNING] Fail to register {0}".format(dst_path)
    ## 하위폴더 탐색 끝


### transifex.com에서 새 번역 가져오기
def pull_trans():
    ## 하위 모든 폴더 탐색 시작
    for (path, dir, files) in os.walk(SRC_DIR):
        # 패턴과 일치 하지 않는 폴더 스킵
        if not checkFolderPattern(path):
            continue

        if files.count("messages.properties") > 0:
            resource_name = getResName(os.path.join(path, "messages.properties"))
            if not resource_name:
                continue

            for lang in LANG_LIST:
                src_path = os.path.join(path, "messages_{0}.properties".format(lang))
                dst_path = os.path.join(DST_DIR, "{0}_{1}.properties".format(resource_name, lang))

                # 없는 언어 스킵
                if not os.path.isfile(src_path):
                    continue

                # 번역 파일 가져오기
                cmd = "tx pull -l {1} -r {2}.{0} -f".format(resource_name, lang, PROJECT_NAME)
                print cmd
                rc = os.system(cmd)
                if rc != 0:
                    print "[WARNING] Fail to pull {0}".format(dst_path)

                # 파일 복사
                print ("copy {0} {1}".format(os.path.abspath(dst_path), os.path.abspath(src_path)))
                shutil.copy2(dst_path, src_path)

        elif files.count("bundle.properties") > 0:
            resource_name = getResName(os.path.join(path, "bundle.properties"))
            if not resource_name:
                continue

            for lang in LANG_LIST:
                src_path = os.path.join(path, "bundle_{0}.properties".format(lang))
                dst_path = os.path.join(DST_DIR, "{0}_{1}.properties".format(resource_name, lang))

                # 없는 언어 스킵
                if not os.path.isfile(src_path):
                    continue

                # 번역 파일 가져오기
                cmd = "tx pull -l {1} -r {2}.{0} -f".format(resource_name, lang, PROJECT_NAME)
                print cmd
                rc = os.system(cmd)
                if rc != 0:
                    print "[WARNING] Fail to pull {0}".format(dst_path)

                # 파일 복사
                print ("copy {0} {1}".format(os.path.abspath(dst_path), os.path.abspath(src_path)))
                shutil.copy2(dst_path, src_path)

    ## 하위폴더 탐색 끝


### 옵션에 따른 기능 실행
def main():
    args = sys.argv
    cmd = os.path.split(args[0])[1]

    if len(args) <= 1:
        option = None
    else:
        option = args[1].lower()

    ## 실행 조건 확인
    if not checkTx():
        return
    if not checkTxInit():
        return

    DST_DIR = os.getcwd()

    if option == None:
        show_help(cmd)
    elif option == "-update":
        push_source()
        pull_trans()
    elif option == "-help":
        show_help(cmd)
    elif option == "-init":
        init_resource()
    elif option == "-push_source":
        push_source()
    elif option == "-push_trans":
        push_trans()
    elif option == "-pull_trans":
        pull_trans()
    elif option == "-init_all":
        init_resource()
        push_source()
        push_trans()
    else:
        show_help(cmd)


### 커맨드로 실행된 경우 main() 실행
if __name__ == "__main__":
    main()