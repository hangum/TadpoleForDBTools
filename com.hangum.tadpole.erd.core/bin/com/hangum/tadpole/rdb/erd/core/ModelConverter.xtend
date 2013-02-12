//package com.hangum.tadpole.erd.core
//
//import com.hangum.tadpole.model.Column
//import com.hangum.tadpole.model.Table
//import com.hangum.tadpole.model.TadpoleFactory
//
//class ModelConverter {
// 		extension JavaExtension = new JavaExtension()
//		
//	 def public test(){
//		var table = newTable[
//			name = "test"
//
//			columns += newColumn[
//				field = "hahaha"
//				type = "nvarchar(200)"
//			]
//
//			columns += newColumn[
//				field = "hahaha"
//				type = "nvarchar(200)"
//			]
//		]
//		
//		table.columns.filter[type == "nvarchar(200)"].forEach[it.type = "nvarchar2(200)"];
//		var nameList = table.columns.map[it.field]
//		for(n : nameList){
//			System::out.println(n)
//		}
//		table.test
//		return table
//	}
//	
//	def dispatch debug(Table t){
//		System::out.println("table")
//	}
//	
//	def dispatch debug(Column c){
//		System::out.println("column")
//	}
//	
//	
//	def newTable((Table)=>void initializer){
//		var table = TadpoleFactory::eINSTANCE.createTable
//		initializer.apply(table)
//		return table
//	}
//	
//	def newColumn((Column)=>void initializer){
//		var col = TadpoleFactory::eINSTANCE.createColumn
//		initializer.apply(col)
//		return col
//	}
//	
//	def html(Table table)'''
//		<table>
//			<tr>
//				<th>«table.name»</th>
//			</tr>
//			«FOR col : table.columns »
//				«col.html»
//			«ENDFOR»
//		</table>
//	'''
//	
//	def html(Column c)'''
//		<tr>
//			<td>
//				«c.field» : «c.type»
//			</td>
//		</tr>
//	'''
//} 