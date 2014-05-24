// args[0]:input filename
// args[1]:template filename
// args[2]:character code

def inputFile = new File(args[0])
def inputs = []
inputFile.readLines(args[2]).each {
  if (!it.empty) {
    inputs << it.split('\t')
  }
}

def templateFileName = args[1]
def f = new File(templateFileName)
def engine = new groovy.text.SimpleTemplateEngine()
def binding = ['values' :  inputs, 'inputFileName' : args[0], 'templateFileName' : templateFileName ]
def template = engine.createTemplate(f).make(binding)

def date = new Date().format('yyyyMMdd_HHmmss')
def result = new File(date + '_'  + templateFileName)
result.write(template.toString(), args[2])
