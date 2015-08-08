start java -jar selenium\selenium-server-standalone-2.47.1.jar -role hub -host 127.0.0.1
start java -jar selenium\selenium-server-standalone-2.47.1.jar -role node  -hub http://127.0.0.1:4444/grid/register 
rem -Dwebdriver.chrome.driver=selenium\chromedriver.exe
