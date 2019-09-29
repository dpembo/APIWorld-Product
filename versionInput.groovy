userInput = input(
id: 'Version Required', message: 'Please provide version number', parameters: [
[$class: 'StringParameterDefinition', defaultValue: "0.0.0", description: '', name: 'Version Number']
])

env.VERSION=userInput