package com.doosan.biz.ddic.hr.tools
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Component
import com.doosan.biz.ddic.hr.system.SafeTool
import java.text.SimpleDateFormat
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.DateUtil
@Component
class SafeTestTool {
	
	static List getData(){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd")
		def datas = []
		//读取数据文件	
		FileInputStream is = new FileInputStream(new File("D:/Development/Excel/TestData-20200528.xlsx"))
		Workbook workbook = WorkbookFactory.create(is)
		Sheet sheet = workbook.getSheetAt(0)
		//获取数据行数
		int rows = sheet.getPhysicalNumberOfRows()	
		//从第二行开始遍历
		for(int r = 1; r < rows; r++){
			def data = [:]
			Row row = sheet.getRow(r)
			int columns = row.getPhysicalNumberOfCells()
			//遍历列
			for(int c = 0; c < columns; c++){
				Cell cell = row.getCell(c)			
				def value = null
				int cellType = cell.getCellType()
				switch(cellType) {
					case Cell.CELL_TYPE_STRING: //文本
						value = cell.getStringCellValue()
						break
					case Cell.CELL_TYPE_NUMERIC: //数字、日期
						if(DateUtil.isCellDateFormatted(cell)) {
							value = formater.format(cell.getDateCellValue()) 	//日期型
						}else {
							if(c == 1)
								value = Math.round(cell.getNumericCellValue() * 100) + "%"
							else
								value = Math.round(cell.getNumericCellValue()) 		//数字( 默认读取的是double类型的数据,此处转换成了long型数据)
						}
						break
					case Cell.CELL_TYPE_BOOLEAN: //布尔型
						value = String.valueOf(cell.getBooleanCellValue())
						break
					case Cell.CELL_TYPE_BLANK: //空白
						value = cell.getStringCellValue()
						break
					case Cell.CELL_TYPE_ERROR: //错误
						value = ""
						break
					case Cell.CELL_TYPE_FORMULA: //公式
						try {
							value = String.valueOf(cell.getNumericCellValue())
						} catch (IllegalStateException e) {
							value = String.valueOf(cell.getRichStringCellValue())
						}
						break
					default:
						value = ""
				}
				if(c == 0)
					data.put("certNo", value)	//身份证号
				if(c == 1)
					data.put("userName", value)	//姓名
				if(c == 2)
					data.put("mobile", value)	//手机号
			}
			datas << data
		}
		if (is != null) {
			is.close()
		}
		if (workbook != null) {
			workbook.close()
		}
		return datas
	}
	
	static void doTest() {
		//Get Excel Data to test
		def data = getData()
		println "Data size is : " + data.size()
		def result = []
		println "*" * 80
		data.each {
			def name = it.getAt('userName').toString().trim()
			def mobile = it.getAt('mobile').toString().trim()
			def id = it.getAt('certNo').toString().trim()
			def tr = [:]
			if("".equals(name) || "".equals(mobile) || "".equals(id)) {
				println "Information is not ok."
			}else {
				println "Name is : $name, mobile is : $mobile, ID is : $id"
				tr = SafeTool.doSafeAction(mobile, name, id)
			}
			it.putAt("tr", tr)
			result << it
			println "*" * 80
		}
		//Write to the Excel
		OutputStream outputStream = new FileOutputStream("D:/Development/Excel/TestData-20200528-result.xlsx")
		//create Excel workbook
		Workbook workbook = new XSSFWorkbook()
		//create sheet
		Sheet sheet = workbook.createSheet()
		sheet.setDefaultColumnWidth((short) 15)
		//create title row
		Row row = sheet.createRow(0)
		def titles = ['身份证号','客户姓名','联系方式','分数','标签','RequestID']
		//insert titles
		(0..5).each {
			Cell cell = row.createCell(it)
			cell.setCellValue(titles[it])
		}
		//insert values
		def rows = result.size()
		(1..rows).each { i ->
			//create row
			row = sheet.createRow(i)
			//create column
			(0..5).each { j ->
				Cell cell = row.createCell(j)
				switch(j) {
					case 0:
						cell.setCellValue(result[i-1].getAt("certNo"))
						break
					case 1:
						cell.setCellValue(result[i-1].getAt("userName"))
						break
					case 2:
						cell.setCellValue(result[i-1].getAt("mobile"))
						break
					case 3:
						cell.setCellValue(result[i-1].getAt("tr").getAt("score"))
						break
					case 4:
						cell.setCellValue(result[i-1].getAt("tr").getAt("tags"))
						break
					case 5:
						cell.setCellValue(result[i-1].getAt("tr").getAt("requestId"))
						break
					default:
						break
				}
			}
		}
		//do create Excel file
		try {
			workbook.write(outputStream)
		} finally {
			if (outputStream != null) {
				outputStream.close()
			}
			if (workbook != null) {
				workbook.close()
			}
		}
		println "Test finished ..."
	}
	
	static void main(String[] args) {
		doTest() 
	}	
}