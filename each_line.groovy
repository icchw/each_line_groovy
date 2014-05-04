def inputFile = new File('input.txt')
def inputs = []
inputFile.readLines("UTF-8").each {
  if (!it.empty) {
    inputs << it.split('\t')
  }
}

def templateFileName = args[0]
def f = new File(templateFileName)
def engine = new groovy.text.SimpleTemplateEngine()
def binding = ['values' :  inputs]
def template = engine.createTemplate(f).make(binding)

def date = new Date().format('yyyyMMdd_HHmmss')
def template_extension = templateFileName.replaceAll(/.*\.(.*)/){it[1]}
def result = new File(date + "." + template_extension)
result.write(template.toString(), "UTF-8")
