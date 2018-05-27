#-------------------------------------------------------------------------------
# Copyright (c) 2013 hangum.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the GNU Lesser Public License v2.1
# which accompanies this distribution, and is available at
# http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
# 
# Contributors:
#     hangum - initial API and implementation
#-------------------------------------------------------------------------------
* tadpole preference  
1. cycle dependiencies를 막기위해 com.hangum.tadpole.db.rap.common는 depenciencies에서 제외합니다. 
	(이유는 preference 정보를 디비에 저장하면서 sql과 의존관계를 가져서입니다) - 다른 아이디어는?????
2. 대신에 필요한 util은 만들어 사용하고 internal package를 사용합니다. 
