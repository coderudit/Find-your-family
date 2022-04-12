package com.findyourfamily.app.app.tests;

import com.findyourfamily.app.Genealogy;
import com.findyourfamily.app.business.familytree.FamilyTree;
import com.findyourfamily.app.business.familytree.IFamilyTree;
import com.findyourfamily.app.database.DatabaseUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FamilyTreeTests {

    private Genealogy genealogy;

    @BeforeEach()
    void intitalize() {
        genealogy = new Genealogy();
    }

    @Test()
    void ShouldAddPerson() {
        try {
            var person = genealogy.addPerson("Udit Gandhi");
            assertNotNull(person);
            DatabaseUtility.closeConnection();
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddPersonBasicAttributes() {
        try {
            var personIdentity = genealogy.findPerson("Udit Gandhi");

            Map<String, String> personAttributes = new HashMap<>();
            personAttributes.put("gender", "Male");
            personAttributes.put("yearofbirth", "1992");
            personAttributes.put("monthofbirth", "7");
            personAttributes.put("dayofbirth", "29");
            //personAttributes.put("locationofbirth", "myanmar");
            //personAttributes.put("occupation", "business");
            //personAttributes.put("notes", "V note 1");
            //personAttributes.put("references", "V reference 1");

            assertEquals(true, genealogy.recordAttributes(personIdentity, personAttributes));
            DatabaseUtility.closeConnection();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test()
    void ShouldAddPersonNewAttributes() {
        try {
            var personIdentity = genealogy.findPerson("Vijay Gandhi");

            Map<String, String> personAttributes = new HashMap<>();
            personAttributes.put("company", "sv investments");
            personAttributes.put("mothername", "Santosh kumari");

            assertEquals(true, genealogy.recordAttributes(personIdentity, personAttributes));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddPerson2() {
        try {
            var person = genealogy.addPerson("Crappy");
            assertNotNull(person);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddPersonBasicAttributes2() {
        try {
            var personIdentity = genealogy.findPerson("Crappy");

            Map<String, String> personAttributes = new HashMap<>();
            personAttributes.put("gender", "Male");
            personAttributes.put("dateofbirth", "29/07/1993");
            personAttributes.put("locationofbirth", "delhi3");
            personAttributes.put("dateofdeath", "29/07/1992");
            personAttributes.put("locationofdeath", "delhi4");
            personAttributes.put("occupation", "Software tester");
            personAttributes.put("notes", "F note 1");
            personAttributes.put("references", "F reference 1");

            assertEquals(true, genealogy.recordAttributes(personIdentity, personAttributes));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddPersonNewAttributes2() {
        IFamilyTree familyTree = new FamilyTree();
        try {
            var personIdentity = genealogy.findPerson("Frappy");

            Map<String, String> personAttributes = new HashMap<>();
            personAttributes.put("motherdob", "14/12/1940");
            personAttributes.put("mothername", "Fhappy singh");

            assertEquals(true, genealogy.recordAttributes(personIdentity, personAttributes));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddReference() {
        try {
            var personIdentity = genealogy.findPerson("Appy");

            assertEquals(true, genealogy.recordReference(personIdentity,
                    "A reference 2"));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddNote() {
        try {
            var personIdentity = genealogy.findPerson("Appy");

            assertEquals(true, genealogy.recordNote(personIdentity,
                    "A note 2"));
        } catch (Exception ex) {

        }
    }

    @Test
    void ShouldRecordParentChild() {
        try {
            var personIdentity1 = genealogy.findPerson("Vijay Gandhi");

            var personIdentity2 = genealogy.findPerson("Udit Gandhi");

            genealogy.recordChild(personIdentity1, personIdentity2);
        } catch (Exception ex) {

        }
    }

    @Test
    void ShouldRecordPartnering() {
        try {
            var personIdentity1 = genealogy.findPerson("Vijay Gandhi");

            var personIdentity2 = genealogy.findPerson("Sudha Gandhi");
            //genealogy.recordDissolution(personIdentity1, personIdentity2);
            genealogy.recordPartnering(personIdentity1, personIdentity2);
        } catch (Exception ex) {

        }
    }

    @Test
    void ShouldRecordDissolution() {
        try {
            var personIdentity1 = genealogy.findPerson("Crappy");
            var personIdentity2 = genealogy.findPerson("Drappy");
            genealogy.recordPartnering(personIdentity1, personIdentity2);
            genealogy.recordChild(personIdentity1, personIdentity2);

            var personIdentity3 = genealogy.findPerson("Appy");
            genealogy.recordChild(personIdentity1, personIdentity3);

            genealogy.recordDissolution(personIdentity1, personIdentity2);

            var personIdentity4 = genealogy.findPerson("Erappy");
            genealogy.recordChild(personIdentity1, personIdentity4);

            var personIdentity5 = genealogy.findPerson("Frappy");
            genealogy.recordChild(personIdentity4, personIdentity5);


        } catch (Exception ex) {

        }
    }

    @Test
    void ShouldAddCorrectRelations() throws IOException, ParseException {

        var person1 = genealogy.findPerson("A");
        var person2 = genealogy.findPerson("B");
        var person3 = genealogy.findPerson("C");
        var person4 = genealogy.findPerson("D");
        var person5 = genealogy.findPerson("E");
        var person6 = genealogy.findPerson("F");
        var person7 = genealogy.findPerson("G");
        var person8 = genealogy.findPerson("H");
        var person9 = genealogy.findPerson("I");
        var person10 = genealogy.findPerson("J");
        var person11 = genealogy.findPerson("K");
        var person12 = genealogy.findPerson("L");
        var person13 = genealogy.findPerson("M");

        //D-> A, B, C
        genealogy.recordChild(person4, person1);
        genealogy.recordChild(person4, person2);
        genealogy.recordChild(person4, person3);

        //G-> E, F
        genealogy.recordChild(person7, person5);
        genealogy.recordChild(person7, person6);

        //I-> G, H
        genealogy.recordChild(person9, person7);
        genealogy.recordChild(person9, person8);

        //L-> K
        genealogy.recordChild(person12, person11);

        //J-> D
        genealogy.recordChild(person10, person4);

        //J+M
        genealogy.recordPartnering(person10, person13);

        //J-> I AND M-> I
        genealogy.recordChild(person10, person9);

        //J-M
        genealogy.recordDissolution(person10, person13);

        //M-> L
        genealogy.recordChild(person13, person12);
    }


}
