set username=Administrator
set password=manage
set url=http://localhost:5555
#set apiID=dda7a194-dced-4c2f-912f-4bd85a3ba965
set apiID=f8fe6c6f-a046-4562-bccb-070439c14c9a
set file=.\microgateway\Product.zip

curl -u %username%:%password% %url%/rest/apigateway/archive?apis=%apiID% --output %file%
