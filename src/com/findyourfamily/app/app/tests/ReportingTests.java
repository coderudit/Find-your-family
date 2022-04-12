package com.findyourfamily.app.app.tests;

import com.findyourfamily.app.models.domain.PersonIdentity;
import com.findyourfamily.app.Genealogy;
import com.findyourfamily.app.business.reporting.IReport;
import com.findyourfamily.app.business.reporting.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReportingTests {

    private Genealogy genealogy;

    @BeforeEach()
    void intitalize() {
        genealogy = new Genealogy();
    }

    @Test()
    void ShouldFindPersonByName() {
        try {
            var personIdentity = genealogy.findPerson("J");
            System.out.println();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test()
    void ShouldFindNameById() {
        try {
            var name = genealogy.findName(2);
            assertNotNull(name);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindNotesAndReferencesById() {
        try {
            var name = genealogy.findPerson("Appy");
            name.setPersonId(100);
            assertNotNull(name);
            var notesAndReferences = genealogy.notesAndReferences(name);
            assertNotNull(notesAndReferences);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindMediaFileByName() {
        try {
            var fileIdentifier = genealogy.findMediaFile("TravelAssistantTest2.java");
            assertNotNull(fileIdentifier);
        } catch (Exception ex) {

        }
    }


    @Test()
    void ShouldFindMediaFileById() {
        try {
            var fileIdentifier = genealogy.findMediaFile(100);
            assertNotNull(fileIdentifier);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindMediaFileByTag() {
        try {
            var files = genealogy.findMediaByTag("tag 1", null,null);
            assertNotNull(files);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindMediaFileByLocation() {
        try {
            var files = genealogy.findMediaByLocation("Halifax city", null, null);
            assertNotNull(files);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindMediaFileByPeople() {
        try {
            var person1 = genealogy.findPerson("Madan lal Gandhi");
            var person2 = genealogy.findPerson("Vijay Gandhi");
            var person3 = genealogy.findPerson("Sudha Gandhi");
            var person4 = genealogy.findPerson("Udit Gandhi");
            var person5 = genealogy.findPerson("Appy");
            Set<PersonIdentity> persons = new HashSet<>();
            persons.add(person1);
            persons.add(person2);
            persons.add(person3);
            persons.add(person4);
            persons.add(person5);
            var files = genealogy.findIndividualsMedia(persons, null, null);
            assertNotNull(files);
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldFindDescendants() {
        Genealogy genealogy = new Genealogy();
        IReport report = new Report();
        try {
            var person = report.findPerson("Udit");
            //var files = genealogy.descendants(person, 2);
            //assertNotNull(files);
        } catch (Exception ex) {

        }
    }
    @Test
    void ShouldFindRelation() throws IOException, ParseException {
        var genealogy = new Genealogy();

        var person1 = genealogy.findPerson("E");
        var person2 = genealogy.findPerson("J");
        //var person3 = genealogy.findPerson("C");
        //var person4 = genealogy.findPerson("D");
        //var person5 = genealogy.findPerson("E");
        //var person6 = genealogy.findPerson("F");
        //var person7 = genealogy.findPerson("G");
        //var person8 = genealogy.findPerson("H");
        //var person9 = genealogy.findPerson("I");
        //var person10 = genealogy.findPerson("J");
        //var person11 = genealogy.findPerson("K");
        //var person12 = genealogy.findPerson("L");
        //var person13 = genealogy.findPerson("M");



        assertNull(genealogy.findRelation(person1, person2));
        System.out.println();
    }
}
