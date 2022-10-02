import java.io.StringWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Register {

    Vector<PersonInfo> people;

    public Register() {
        people = new Vector<PersonInfo>();
    }

    public boolean ReadRowBased(String input) throws NullPointerException
    {
        String[] lines = input.split("\\n");
        
        PersonInfo currPerson = null;
        PersonBase currToEdit = null;

        for(String line : lines)
        {
            String[] arr = line.split("\\|", 2);
            switch(arr[0]){
                case "P":
                    currPerson = ReadPerson(arr[1]);
                    currToEdit = currPerson;
                    people.add((PersonInfo)currPerson);
                break;
                case "T":
                    if(currToEdit == null)
                        return false;
                    else
                        ReadTele(currToEdit, arr[1]);
                break;
                case "A":
                    if(currToEdit == null)
                        return false;
                    else
                        ReadAdress(currToEdit, arr[1]);
                break;
                case "F":
                    if(currPerson == null)
                        return false;
                    else
                        currToEdit = ReadFamilyMember(currPerson, arr[1]);
                break;
                default:
                return false;
            }
        }

        if(people.size() == 0) {
            return false;
        }
        return true;
    }

    public String OutputInXML(){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document dom = db.newDocument();

        Element rootEle = dom.createElement("people");

        for(PersonInfo info : people) {
            rootEle.appendChild(info.appendToElement(dom, rootEle));
        }

        dom.appendChild(rootEle);

        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty("omit-xml-declaration", "yes");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            tr.transform(new DOMSource(dom), 
                                 new StreamResult(writer));
            return writer.getBuffer().toString();

         } catch (TransformerException te) {
            System.out.println(te.getMessage());
            return "";
         }
     } catch (ParserConfigurationException pce) {
        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
     }
        return "";
    }

    private PersonInfo ReadPerson(String input) {
        String[] arr = input.split("\\|");
        PersonInfo info = new PersonInfo();

        if(arr.length > 1) {
            info.firstName = arr[0];
            info.lastName = arr[1];
        }
        else if(arr.length > 0) {
            info.firstName = arr[0];
        }

        return info;
    }

    private void ReadTele(PersonBase person, String input) {
        String[] arr = input.split("\\|");
        TeleInfo info = new TeleInfo();
        if(arr.length > 1) {
            info.mobileNumber = arr[0];
            info.landlineNumber = arr[1];
        }
        else if(arr.length > 0) {
            info.mobileNumber = arr[0];
        }
        person.teleInfo = info;
    }

    private void ReadAdress(PersonBase person, String input) {
        String[] arr = input.split("\\|");
        AdressInfo info = new AdressInfo();

        if(arr.length > 2) {
            info.street = arr[0];
            info.city = arr[1];
            info.zipcode = arr[2];
        }
        else if(arr.length > 1) {
            info.street = arr[0];
            info.city = arr[1];
        }
        else if(arr.length > 0) {
            info.street = arr[0];
        }
        person.adressInfo = info;
    }

    private FamilyMemberInfo ReadFamilyMember(PersonInfo person, String input)
    {
        String[] arr = input.split("\\|");
        FamilyMemberInfo info = new FamilyMemberInfo();

        if(arr.length > 1) {
            info.name = arr[0];
            info.birthYear = arr[1];
        }
        else if(arr.length > 0) {
            info.name = arr[0];
        }

        person.family.add(info);
        return info;
    }
}

class PersonBase {
    TeleInfo teleInfo;
    AdressInfo adressInfo;

    PersonBase() {
        teleInfo = new TeleInfo();
        adressInfo = new AdressInfo();
    }
}

class PersonInfo extends PersonBase {
    String firstName;
    String lastName;

    Vector<FamilyMemberInfo> family;

    public PersonInfo() {
        firstName = "";
        lastName = "";
        family = new Vector<FamilyMemberInfo>();
    }

    public Element appendToElement(Document dom, Element parent) {
        Element e = dom.createElement("person");

        parent.appendChild(e);

        Element firstNameElement = dom.createElement("firstname");
        firstNameElement.appendChild(dom.createTextNode(firstName));
        e.appendChild(firstNameElement);

        Element lastNameElement = dom.createElement("lastname");
        lastNameElement.appendChild(dom.createTextNode(lastName));
        e.appendChild(lastNameElement);

        adressInfo.appendToElement(dom, e);
        teleInfo.appendToElement(dom, e);

        for(FamilyMemberInfo member : family) {
            member.appendToElement(dom, e);
        }

        return e;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj == null)
            return false;
        else if(obj.getClass() != this.getClass())
            return false;
        PersonInfo other = (PersonInfo) obj;

        if(family.size() != other.family.size())
            return false;

        for(int i = 0; i < family.size(); i++) {
            if(!family.get(i).equals(other.family.get(i)))
                return false;
        }

        return this.firstName.equals(other.firstName) &&
               this.lastName.equals(other.lastName) && 
               this.teleInfo.equals(other.teleInfo) &&
               this.adressInfo.equals(other.adressInfo);
    }
}

class TeleInfo {
    String mobileNumber;
    String landlineNumber;

    public TeleInfo() {
        mobileNumber = "";
        landlineNumber = "";
    }

    public Element appendToElement(Document dom, Element parent) {
        Element e = null;
        if(!mobileNumber.isEmpty() || !landlineNumber.isEmpty() ) {
            e = dom.createElement("phone");
            parent.appendChild(e);
        }

        if(!mobileNumber.isEmpty()) {
            Element streetElement = dom.createElement("mobile");
            streetElement.appendChild(dom.createTextNode(mobileNumber));
            e.appendChild(streetElement);
        }

        if(!landlineNumber.isEmpty()) {
            Element cityElement = dom.createElement("landline");
            cityElement.appendChild(dom.createTextNode(landlineNumber));
            e.appendChild(cityElement);
        }

        return e;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj == null)
            return false;
        else if(obj.getClass() != this.getClass())
            return false;
        TeleInfo other = (TeleInfo) obj;
        

        return this.mobileNumber.equals(other.mobileNumber) && 
               this.landlineNumber.equals(other.landlineNumber);
    }
}

class AdressInfo {
    String street;
    String city;
    String zipcode;

    public AdressInfo() {
        street = "";
        city = "";
        zipcode = "";
    }

    public Element appendToElement(Document dom, Element parent) {
        Element e = null;
        if(!street.isEmpty() || !city.isEmpty() || !zipcode.isEmpty()) {
            e = dom.createElement("adress");
            parent.appendChild(e);
        }

        if(!street.isEmpty()) {
            Element streetElement = dom.createElement("street");
            streetElement.appendChild(dom.createTextNode(street));
            e.appendChild(streetElement);
        }

        if(!city.isEmpty()) {
            Element cityElement = dom.createElement("city");
            cityElement.appendChild(dom.createTextNode(city));
            e.appendChild(cityElement);
        }

        if(!zipcode.isEmpty()) {
            Element zipcodeElement = dom.createElement("zipcode");
            zipcodeElement.appendChild(dom.createTextNode(zipcode));
            e.appendChild(zipcodeElement);
        }

        return e;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj == null)
            return false;
        else if(obj.getClass() != this.getClass())
            return false;
            AdressInfo other = (AdressInfo) obj;
        

        return this.street.equals(other.street) && 
               this.city.equals(other.city) && 
               this.zipcode.equals(other.zipcode);
    }
}

class FamilyMemberInfo extends PersonBase {
    String name;
    String birthYear;

    FamilyMemberInfo() {

    }

    public Element appendToElement(Document dom, Element parent) {
        Element e = null;
        if(!name.isEmpty() || !birthYear.isEmpty()) {
            e = dom.createElement("family");
            parent.appendChild(e);
        }

        if(!name.isEmpty()) {
            Element streetElement = dom.createElement("name");
            streetElement.appendChild(dom.createTextNode(name));
            e.appendChild(streetElement);
        }

        if(!birthYear.isEmpty()) {
            Element cityElement = dom.createElement("born");
            cityElement.appendChild(dom.createTextNode(birthYear));
            e.appendChild(cityElement);
        }

        adressInfo.appendToElement(dom, e);
        teleInfo.appendToElement(dom, e);

        return e;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        else if(obj == null)
            return false;
        else if(obj.getClass() != this.getClass())
            return false;
        FamilyMemberInfo other = (FamilyMemberInfo) obj;

        return this.name.equals(other.name) &&
               this.birthYear.equals(other.birthYear) && 
               this.teleInfo.equals(other.teleInfo) &&
               this.adressInfo.equals(other.adressInfo);
    }
}