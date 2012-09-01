#-------------------------------------------------------------------------------
# Copyright (c) 2012 Cho Hyun Jong.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
# 
# Contributors:
#     Cho Hyun Jong - initial API and implementation
#-------------------------------------------------------------------------------
* tadpole preference  
1. cycle dependiencies를 막기위해 com.hangum.tadpole.db.rap.common는 depenciencies에서 제외합니다. 
	(이유는 preference 정보를 디비에 저장하면서 sql과 의존관계를 가져서입니다) - 다른 아이디어는?????
2. 대신에 필요한 util은 만들어 사용하고 internal package를 사용합니다. 
