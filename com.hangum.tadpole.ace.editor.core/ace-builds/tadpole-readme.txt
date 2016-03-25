ace에디터는 다음음 확장했습니다.
https://github.com/ajaxorg/ace-builds/

		1.1.6 버전. -  2014.09.14
		2015.04.03 Version 1.1.9

												15.05.14-hangum 

1. ace.js의 파일에 this.getSyntaxFoldRange 메소드를 추가해야합니다.
/**
     *  Tadpole modification 
     *  sql folding
     */
    this.getSyntaxFoldRange = function(row, column, dir) {
        var iterator = new TokenIterator(this, row, column);
        var token = iterator.getCurrentToken();
        // 키워드인지 확인.
        if (token && /^keyword/.test(token.type)) {
            var range = new Range();
            var re = /[.\s]?\;[.\s]?$/;
            
            // 키워드 시작부분은 남기고 단축[...]처리하도록.
            range.start.row = iterator.getCurrentTokenRow();
            range.start.column = iterator.getCurrentTokenColumn() + token.value.length;

            // 키워드 위치에서 부터 에디터의 첫행을 읽는다.
            iterator = new TokenIterator(this, row, column);            
            if (dir != -1) {
                do {
                	// 계속해서 다음행을 읽으면서 세미콜론(;)으로 종료하는 문자열의 위치를 찾는다.
                    token = iterator.stepForward();
                } while(token && !re.test(token.value));
            } else
                token = iterator.getCurrentToken();

            // 검색된 행과 열을 리턴한다.
            range.end.row = iterator.getCurrentTokenRow();
            range.end.column = iterator.getCurrentTokenColumn() + token.value.length - 1;
            return range;
        }
    };
    
2. src-noconflict/snippets/sqlite.js, src-noconflict/mode-sqlite.js 는 tadpole에서 작성한 것이므로 추가해야합니다. 
3. 나머지 sql 파일들은 기존 ace 에디터를 가져와서 폴딩 기능을 수정한것입니다.  
	제일 하단의 폴딩 함수와, 폴딩을 조절하는 코드를 수정해야합니다. 
	복잡하면, 기존의 커밋 히스토리를 참고합니다. 
4. javascript 압축은 : https://marketplace.eclipse.org/content/yuicompressor 를 사용하였슴.
	-- ace.js의 압축은 효휼이 높은 : http://jscompress.com/ 를 이용했습니다.
