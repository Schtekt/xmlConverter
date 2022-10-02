import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RegisterTest {
    @Test
    public void init() {
        Register reg = new Register();
        assertNotNull(reg);
    }

    PersonInfo createBasePerson() {
        PersonInfo info = new PersonInfo();
        info.firstName = "Carl Gustaf";
        info.lastName = "Bernadotte";
        return info;
    }

    PersonInfo createPersonWithAdress() {
        PersonInfo info = new PersonInfo();
        info.firstName = "Barack";
        info.lastName = "Obama";
        info.adressInfo.street = "1600 Pennsylvania Avenue";
        info.adressInfo.city = "Washington, D.C";
        return info;
    }

    TeleInfo createBaseTeleInfo() {
        TeleInfo info = new TeleInfo();
        info.mobileNumber = "0768-101801";
        info.landlineNumber = "08-101801";
        return info;
    }

    AdressInfo createBaseAdressInfo() {
        AdressInfo info = new AdressInfo();
        info.street = "Drottningholms slott";
        info.city = "Stockholm";
        info.zipcode = "10001";
        return info;
    }

    FamilyMemberInfo createBaseFamilyMember() {
        FamilyMemberInfo info = new FamilyMemberInfo();
        info.name = "Victoria";
        info.birthYear = "1977";
        return info;
    }

    FamilyMemberInfo createFamilyMemberWithAdress() {
        FamilyMemberInfo info = new FamilyMemberInfo();
        info.name = "Victoria";
        info.birthYear = "1977";

        info.adressInfo.street = "Haga Slott";
        info.adressInfo.city = "Stockholm";
        info.adressInfo.zipcode = "10002";
        return info;
    }

    FamilyMemberInfo createFamilyMemberWithPhone() {
        FamilyMemberInfo info = new FamilyMemberInfo();
        info.name = "Carl Philip";
        info.birthYear = "1979";

        info.teleInfo.mobileNumber = "0768-101802";
        info.teleInfo.landlineNumber = "08-101802";
        return info;
    }

    @Test
    public void RegisterPerson() {
        Register reg = new Register();
        PersonInfo expectedInfo = createBasePerson();

        reg.ReadRowBased("P|" + expectedInfo.firstName + "|" + expectedInfo.lastName);
        assertTrue(reg.people.size() == 1);
        assertEquals(expectedInfo, reg.people.get(0));
    }

    @Test
    public void RegisterPhone() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.teleInfo = createBaseTeleInfo();

        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "T|" + expectedPersonInfo.teleInfo.mobileNumber + "|" + expectedPersonInfo.teleInfo.landlineNumber);
        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterAdress() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.adressInfo = createBaseAdressInfo();

        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "A|" + expectedPersonInfo.adressInfo.street + "|" + expectedPersonInfo.adressInfo.city + "|" + expectedPersonInfo.adressInfo.zipcode);

        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterPhoneAndAdress() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.teleInfo = createBaseTeleInfo();
        expectedPersonInfo.adressInfo = createBaseAdressInfo();

        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "T|" + expectedPersonInfo.teleInfo.mobileNumber + "|" + expectedPersonInfo.teleInfo.landlineNumber + "\n" +
                         "A|" + expectedPersonInfo.adressInfo.street + "|" + expectedPersonInfo.adressInfo.city + "|" + expectedPersonInfo.adressInfo.zipcode);

        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterFamilyMember() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.family.add(createBaseFamilyMember());
        
        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "F|" + expectedPersonInfo.family.get(0).name + "|" + expectedPersonInfo.family.get(0).birthYear);
        
        assertTrue(expectedPersonInfo.family.size() == 1);
        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterMultipleFamilyMembers() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.family.add(createBaseFamilyMember());
        expectedPersonInfo.family.add(createBaseFamilyMember());
        
        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "F|" + expectedPersonInfo.family.get(0).name + "|" + expectedPersonInfo.family.get(0).birthYear + "\n" +
                         "F|" + expectedPersonInfo.family.get(1).name + "|" + expectedPersonInfo.family.get(1).birthYear);
        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterFullPersonInfo() {
        Register reg = new Register();

        PersonInfo expectedPersonInfo = createBasePerson();
        expectedPersonInfo.teleInfo = createBaseTeleInfo();
        expectedPersonInfo.adressInfo = createBaseAdressInfo();
        expectedPersonInfo.family.add(createFamilyMemberWithAdress());
        expectedPersonInfo.family.add(createFamilyMemberWithPhone());

        reg.ReadRowBased("P|" + expectedPersonInfo.firstName + "|" + expectedPersonInfo.lastName + "\n" +
                         "T|" + expectedPersonInfo.teleInfo.mobileNumber + "|" + expectedPersonInfo.teleInfo.landlineNumber + "\n" +
                         "A|" + expectedPersonInfo.adressInfo.street + "|" + expectedPersonInfo.adressInfo.city + "|" + expectedPersonInfo.adressInfo.zipcode + "\n" +
                         "F|" + expectedPersonInfo.family.get(0).name + "|" + expectedPersonInfo.family.get(0).birthYear + "\n" +
                         "A|" + expectedPersonInfo.family.get(0).adressInfo.street + "|" + expectedPersonInfo.family.get(0).adressInfo.city + "|" + expectedPersonInfo.family.get(0).adressInfo.zipcode + "\n" +
                         "F|" + expectedPersonInfo.family.get(1).name + "|" + expectedPersonInfo.family.get(1).birthYear + "\n" +
                         "T|" + expectedPersonInfo.family.get(1).teleInfo.mobileNumber + "|" + expectedPersonInfo.family.get(1).teleInfo.landlineNumber);

        assertEquals(expectedPersonInfo, reg.people.get(0));
    }

    @Test
    public void RegisterMultiplePeople() {
        Register reg = new Register();

        PersonInfo expectedFirstPersonInfo = createBasePerson();
        PersonInfo expectedSecondPersonInfo = createPersonWithAdress();

        reg.ReadRowBased("P|" + expectedFirstPersonInfo.firstName + "|" + expectedFirstPersonInfo.lastName + "\n" +
                         "P|" + expectedSecondPersonInfo.firstName + "|" + expectedSecondPersonInfo.lastName + "\n" +
                         "A|" + expectedSecondPersonInfo.adressInfo.street + "|" + expectedSecondPersonInfo.adressInfo.city);

        assertTrue(reg.people.size() == 2);
        assertEquals(expectedFirstPersonInfo, reg.people.get(0));
        assertEquals(expectedSecondPersonInfo, reg.people.get(1));
    }

    @Test
    public void RegisterMultiplePeopleAndFamily() {
        Register reg = new Register();

        PersonInfo expectedFirstPersonInfo = createBasePerson();
        expectedFirstPersonInfo.teleInfo = createBaseTeleInfo();
        expectedFirstPersonInfo.adressInfo = createBaseAdressInfo();
        expectedFirstPersonInfo.family.add(createFamilyMemberWithAdress());
        expectedFirstPersonInfo.family.add(createFamilyMemberWithPhone());
        PersonInfo expectedSecondPersonInfo = createPersonWithAdress();

        reg.ReadRowBased("P|" + expectedFirstPersonInfo.firstName + "|" + expectedFirstPersonInfo.lastName + "\n" +
                         "T|" + expectedFirstPersonInfo.teleInfo.mobileNumber + "|" + expectedFirstPersonInfo.teleInfo.landlineNumber + "\n" +
                         "A|" + expectedFirstPersonInfo.adressInfo.street + "|" + expectedFirstPersonInfo.adressInfo.city + "|" + expectedFirstPersonInfo.adressInfo.zipcode + "\n" +
                         "F|" + expectedFirstPersonInfo.family.get(0).name + "|" + expectedFirstPersonInfo.family.get(0).birthYear + "\n" +
                         "A|" + expectedFirstPersonInfo.family.get(0).adressInfo.street + "|" + expectedFirstPersonInfo.family.get(0).adressInfo.city + "|" + expectedFirstPersonInfo.family.get(0).adressInfo.zipcode + "\n" +
                         "F|" + expectedFirstPersonInfo.family.get(1).name + "|" + expectedFirstPersonInfo.family.get(1).birthYear + "\n" +
                         "T|" + expectedFirstPersonInfo.family.get(1).teleInfo.mobileNumber + "|" + expectedFirstPersonInfo.family.get(1).teleInfo.landlineNumber + "\n" +
                         "P|" + expectedSecondPersonInfo.firstName + "|" + expectedSecondPersonInfo.lastName + "\n" +
                         "A|" + expectedSecondPersonInfo.adressInfo.street + "|" + expectedSecondPersonInfo.adressInfo.city);

        assertTrue(reg.people.size() == 2);
        assertEquals(expectedFirstPersonInfo, reg.people.get(0));
        assertEquals(expectedSecondPersonInfo, reg.people.get(1));
    }

    public Document CreateDoc() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();

        } catch (Exception e) {
            return null;
        }

        return db.newDocument();
    }

    public Element CreateRootElement(Document doc) {
        return doc.createElement("people");
    }

    @Test
    public void OutputPerson() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        reg.people.add(info);
        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" + 
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputPersonWithAdress() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        AdressInfo adressInfo = createBaseAdressInfo();
        info.adressInfo = adressInfo;
        reg.people.add(info);
        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>" + info.adressInfo.street + "</street>\r\n" +
            "            <city>" + info.adressInfo.city + "</city>\r\n" +
            "            <zipcode>" + info.adressInfo.zipcode +"</zipcode>\r\n" +
            "        </adress>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputPersonWithPhone() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        TeleInfo teleInfo = createBaseTeleInfo();
        info.teleInfo = teleInfo;
        reg.people.add(info);
        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <phone>\r\n" +
            "            <mobile>" + info.teleInfo.mobileNumber + "</mobile>\r\n" +
            "            <landline>" + info.teleInfo.landlineNumber + "</landline>\r\n" +
            "        </phone>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputPersonWithPhoneAndAdress() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        AdressInfo adressInfo = createBaseAdressInfo();
        info.adressInfo = adressInfo;
        TeleInfo teleInfo = createBaseTeleInfo();
        info.teleInfo = teleInfo;
        reg.people.add(info);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>" + info.adressInfo.street + "</street>\r\n" +
            "            <city>" + info.adressInfo.city + "</city>\r\n" +
            "            <zipcode>" + info.adressInfo.zipcode +"</zipcode>\r\n" +
            "        </adress>\r\n" +
            "        <phone>\r\n" +
            "            <mobile>" + info.teleInfo.mobileNumber + "</mobile>\r\n" +
            "            <landline>" + info.teleInfo.landlineNumber + "</landline>\r\n" +
            "        </phone>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputPersonFamilyMember() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        FamilyMemberInfo famInfo = createBaseFamilyMember();
        info.family.add(famInfo);
        reg.people.add(info);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <family>\r\n" +
            "            <name>" + info.family.get(0).name + "</name>\r\n" +
            "            <born>" + info.family.get(0).birthYear + "</born>\r\n" +
            "        </family>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputMultipleFamilyMember() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        FamilyMemberInfo famInfo = createBaseFamilyMember();
        FamilyMemberInfo famInfoWithPhone = createFamilyMemberWithPhone();
        info.family.add(famInfo);
        info.family.add(famInfoWithPhone);
        reg.people.add(info);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <family>\r\n" +
            "            <name>" + info.family.get(0).name + "</name>\r\n" +
            "            <born>" + info.family.get(0).birthYear + "</born>\r\n" +
            "        </family>\r\n" +
            "        <family>\r\n" +
            "            <name>" + info.family.get(1).name + "</name>\r\n" +
            "            <born>" + info.family.get(1).birthYear + "</born>\r\n" +
            "            <phone>\r\n" +
            "                <mobile>" + info.family.get(1).teleInfo.mobileNumber + "</mobile>\r\n" +
            "                <landline>" + info.family.get(1).teleInfo.landlineNumber + "</landline>\r\n" +
            "            </phone>\r\n" +
            "        </family>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputFullPersonInfo() {
        Register reg = new Register();
        PersonInfo info = createBasePerson();
        FamilyMemberInfo famInfoWithAdress = createFamilyMemberWithAdress();
        FamilyMemberInfo famInfoWithPhone = createFamilyMemberWithPhone();
        info.family.add(famInfoWithAdress);
        info.family.add(famInfoWithPhone);
        reg.people.add(info);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + info.firstName + "</firstname>\r\n" +
            "        <lastname>" + info.lastName + "</lastname>\r\n" +
            "        <family>\r\n" +
            "            <name>" + info.family.get(0).name + "</name>\r\n" +
            "            <born>" + info.family.get(0).birthYear + "</born>\r\n" +
            "            <adress>\r\n" +
            "                <street>" + info.family.get(0).adressInfo.street + "</street>\r\n" +
            "                <city>" + info.family.get(0).adressInfo.city + "</city>\r\n" +
            "                <zipcode>" + info.family.get(0).adressInfo.zipcode +"</zipcode>\r\n" +
            "            </adress>\r\n" +
            "        </family>\r\n" +
            "        <family>\r\n" +
            "            <name>" + info.family.get(1).name + "</name>\r\n" +
            "            <born>" + info.family.get(1).birthYear + "</born>\r\n" +
            "            <phone>\r\n" +
            "                <mobile>" + info.family.get(1).teleInfo.mobileNumber + "</mobile>\r\n" +
            "                <landline>" + info.family.get(1).teleInfo.landlineNumber + "</landline>\r\n" +
            "            </phone>\r\n" +
            "        </family>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputMultiplePeople() {
        Register reg = new Register();
        PersonInfo firstPersonInfo = createBasePerson();
        PersonInfo secondPersonInfo = createPersonWithAdress();
        reg.people.add(firstPersonInfo);
        reg.people.add(secondPersonInfo);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + firstPersonInfo.firstName + "</firstname>\r\n" +
            "        <lastname>" + firstPersonInfo.lastName + "</lastname>\r\n" +
            "    </person>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + secondPersonInfo.firstName + "</firstname>\r\n" +
            "        <lastname>" + secondPersonInfo.lastName + "</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>" + secondPersonInfo.adressInfo.street + "</street>\r\n" +
            "            <city>" + secondPersonInfo.adressInfo.city + "</city>\r\n" +
            "        </adress>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void OutputMultiplePeopleAndFamily() {
        Register reg = new Register();
        PersonInfo firstPersonInfo = createBasePerson();
        FamilyMemberInfo famInfoWithAdress = createFamilyMemberWithAdress();
        FamilyMemberInfo famInfoWithPhone = createFamilyMemberWithPhone();
        firstPersonInfo.family.add(famInfoWithAdress);
        firstPersonInfo.family.add(famInfoWithPhone);
        reg.people.add(firstPersonInfo);
        PersonInfo secondPersonInfo = createPersonWithAdress();
        reg.people.add(secondPersonInfo);

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + firstPersonInfo.firstName + "</firstname>\r\n" +
            "        <lastname>" + firstPersonInfo.lastName + "</lastname>\r\n" +
            "        <family>\r\n" +
            "            <name>" + firstPersonInfo.family.get(0).name + "</name>\r\n" +
            "            <born>" + firstPersonInfo.family.get(0).birthYear + "</born>\r\n" +
            "            <adress>\r\n" +
            "                <street>" + firstPersonInfo.family.get(0).adressInfo.street + "</street>\r\n" +
            "                <city>" + firstPersonInfo.family.get(0).adressInfo.city + "</city>\r\n" +
            "                <zipcode>" + firstPersonInfo.family.get(0).adressInfo.zipcode +"</zipcode>\r\n" +
            "            </adress>\r\n" +
            "        </family>\r\n" +
            "        <family>\r\n" +
            "            <name>" + firstPersonInfo.family.get(1).name + "</name>\r\n" +
            "            <born>" + firstPersonInfo.family.get(1).birthYear + "</born>\r\n" +
            "            <phone>\r\n" +
            "                <mobile>" + firstPersonInfo.family.get(1).teleInfo.mobileNumber + "</mobile>\r\n" +
            "                <landline>" + firstPersonInfo.family.get(1).teleInfo.landlineNumber + "</landline>\r\n" +
            "            </phone>\r\n" +
            "        </family>\r\n" +
            "    </person>\r\n" +
            "    <person>\r\n" +
            "        <firstname>" + secondPersonInfo.firstName + "</firstname>\r\n" +
            "        <lastname>" + secondPersonInfo.lastName + "</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>" + secondPersonInfo.adressInfo.street + "</street>\r\n" +
            "            <city>" + secondPersonInfo.adressInfo.city + "</city>\r\n" +
            "        </adress>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";
        String actualOutput = reg.OutputInXML();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void FullTest() {
        Register reg = new Register();

        reg.ReadRowBased("P|Carl Gustaf|Bernadotte\n"+
        "T|0768-101801|08-101801\n"+
        "A|Drottningholms slott|Stockholm|10001\n" +
        "F|Victoria|1977\n" +
        "A|Haga Slott|Stockholm|10002\n" +
        "F|Carl Philip|1979\n" +
        "T|0768-101802|08-101802\n" +
        "P|Barack|Obama\n" +
        "A|1600 Pennsylvania Avenue|Washington, D.C\n"
        );

        String expectedOutput =
            "<people>\r\n" +
            "    <person>\r\n" +
            "        <firstname>Carl Gustaf</firstname>\r\n" +
            "        <lastname>Bernadotte</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>Drottningholms slott</street>\r\n" +
            "            <city>Stockholm</city>\r\n" +
            "            <zipcode>10001</zipcode>\r\n" +
            "        </adress>\r\n" +
            "        <phone>\r\n" +
            "            <mobile>0768-101801</mobile>\r\n" +
            "            <landline>08-101801</landline>\r\n" +
            "        </phone>\r\n" +
            "        <family>\r\n" +
            "            <name>Victoria</name>\r\n" +
            "            <born>1977</born>\r\n" +
            "            <adress>\r\n" +
            "                <street>Haga Slott</street>\r\n" +
            "                <city>Stockholm</city>\r\n" +
            "                <zipcode>10002</zipcode>\r\n" +
            "            </adress>\r\n" +
            "        </family>\r\n" +
            "        <family>\r\n" +
            "            <name>Carl Philip</name>\r\n" +
            "            <born>1979</born>\r\n" +
            "            <phone>\r\n" +
            "                <mobile>0768-101802</mobile>\r\n" +
            "                <landline>08-101802</landline>\r\n" +
            "            </phone>\r\n" +
            "        </family>\r\n" +
            "    </person>\r\n" +
            "    <person>\r\n" +
            "        <firstname>Barack</firstname>\r\n" +
            "        <lastname>Obama</lastname>\r\n" +
            "        <adress>\r\n" +
            "            <street>1600 Pennsylvania Avenue</street>\r\n" +
            "            <city>Washington, D.C</city>\r\n" +
            "        </adress>\r\n" +
            "    </person>\r\n" +
            "</people>\r\n";

        String actualOutput = reg.OutputInXML();
        assertEquals(expectedOutput, actualOutput);
    }
}
