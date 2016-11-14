# jackson-1451-typeinfo-testing

Test case to reproduce https://github.com/FasterXML/jackson-databind/issues/1451

Simply run maven with different version of jackson :
```
mvn -Djackson.version=2.3.3 test
=> Success
mvn -Djackson.version=2.4.6 test
=> Success
mvn -Djackson.version=2.5.0 test
=> Failure
mvn -Djackson.version=2.5.5 test
=> Failure
mvn -Djackson.version=2.8.5 test
=> Failure
```

