/**
 * args[0] input filename
 * args[1] template filename
 * args[2] character code (default: UTF-8)
 * depends on:
 *   dom4j-1.6.1.jar
 *   gexcelapi-0.3-SNAPSHOT.jar
 *   poi-3.8.jar
 *   poi-ooxml-3.8.jar
 *   poi-ooxml-schemas-3.8.jar
 *   xmlbeans-2.3.0.jar
 */

import org.apache.poi.ss.usermodel.*
import org.jggug.kobo.gexcelapi.*

def splitFileName(fileName) {
  return (fileName =~ /(.+)(\.[^.]+$)/)[0]
}

def inputFileName = args[0]
def inputs = []
def characterCode = args.length >= 3 ? args[2] : "UTF-8"

if (args[0] ==~ /.*xlsx?/) {
  // Excel
  // expand Cell class
  Cell.metaClass.define {
    getEvaluatedValue{
      switch (delegate.cellType) {
        case Cell.CELL_TYPE_FORMULA:
          def workbook = delegate.sheet.workbook
          return workbook.getCreationHelper().createFormulaEvaluator().evaluateInCell(delegate).value
        default :
          return delegate.value
      }
    }
  }

  def book = GExcel.open(inputFileName)
  def sheet = book[0]
    sheet.eachWithIndex {row, i ->
    if (i == 0) {
      // skip header row
      return
    }
    inputs << row.collect{cell -> cell.evaluatedValue}
  }
} else {
  // text
  def inputFile = new File(inputFileName)
  inputFile.readLines(characterCode).each {
    if (!it.empty) {
      inputs << (it.split('\t') as ArrayList)
    }
  }
}

def templateFileName = args[1]
def f = new File(templateFileName)
def engine = new groovy.text.SimpleTemplateEngine()
def binding = ['values' :  inputs, 'inputFileName' : args[0], 'templateFileName' : templateFileName ]
def template = engine.createTemplate(f).make(binding)

def date = new Date().format('yyyyMMdd_HHmmss')
// def result = new File(date + '_'  + templateFileName)
def resultFileName = date + '_' + splitFileName(inputFileName)[1] + splitFileName(templateFileName)[2]
def result = new File(resultFileName)
result.write(template.toString(), characterCode)
