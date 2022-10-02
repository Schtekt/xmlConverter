# xmlConverter
This is a small java program that was created for a coding test.

The goal is to convert information from a line by line system to the XML format.

## The task

The input file format is:

P|first name|surname
T|mobile number|landline number
A|street|city|zipcode
F|name|year of birth

where P may be followed by T, A and F
and F may be followed by T and A.

Additionally, P may be followed by multiple F, and in some cases, entries may be incomplete.

## Example
A given example, the input of
```
P|Carl Gustaf|Bernadotte
T|0768-101801|08-101801
A|Drottningholms slott|Stockholm|10001
F|Victoria|1977
A|Haga Slott|Stockholm|10002
F|Carl Philip|1979
T|0768-101802|08-101802
P|Barack|Obama
A|1600 Pennsylvania Avenue|Washington, D.C
```

Would equal the following output in the xml format
```xml
<people>
    <person>
        <firstname>Carl Gustaf</firstname>
        <lastname>Bernadotte</lastname>
        <adress>
            <street>Drottningholms slott</street>
            <city>Stockholm</city>
            <zipcode>10001</zipcode>
        </adress>
        <phone>
            <mobile>0768-101801</mobile>
            <landline>08-101801</landline>
        </phone>
        <family>
            <name>Victoria</name>
            <born>1977</born>
            <adress>
                <street>Haga Slott</street>
                <city>Stockholm</city>
                <zipcode>10002</zipcode>
            </adress>
        </family>
        <family>
            <name>Carl Philip</name>
            <born>1979</born>
            <phone>
                <mobile>0768-101802</mobile>
                <landline>08-101802</landline>
            </phone>
        </family>
    </person>
    <person>
        <firstname>Barack</firstname>
        <lastname>Obama</lastname>
        <adress>
            <street>1600 Pennsylvania Avenue</street>
            <city>Washington, D.C</city>
        </adress>
    </person>
</people>
```

## About the project
Maven is used to manage the project. Use relevant commands to run and/or install the application.