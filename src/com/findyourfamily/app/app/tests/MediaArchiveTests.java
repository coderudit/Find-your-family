package com.findyourfamily.app.app.tests;

import com.findyourfamily.app.Genealogy;
import com.findyourfamily.app.models.domain.PersonIdentity;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MediaArchiveTests {

    private Genealogy genealogy;

    @BeforeEach()
    void intitalize() {
        genealogy = new Genealogy();
    }

    @Test()
    void ShouldAddMediaFile() {
        try {
            var file = genealogy.addMediaFile
                    ("E:\\DAL\\1. Fall Term 21\\1. CSCI 3901\\Assignment 3 Git\\src\\com\\routeplanner\\app\\tests\\CrapFile3.java");
            assertNotNull(file);
            System.out.println();
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddMediaFileBasicAttributes() {
        try {
            var fileIdentifier = genealogy.findMediaFile("CrapFile3.java");
            Map<String, String> mediaAttributes = new HashMap<>();
            mediaAttributes.put("yearofpicture", "1929");
            mediaAttributes.put("monthofpicture", "11");
            mediaAttributes.put("dayofpicture", "10");
            mediaAttributes.put("locationofpicture", "Halifax city");
            assertNotNull(genealogy.recordMediaAttributes(fileIdentifier, mediaAttributes));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddMediaFileAdditionalAttributes() {
        try {
            var fileIdentifier = genealogy.findMediaFile("TravelAssistantTest1.java");
            Map<String, String> mediaAttributes = new HashMap<>();
            mediaAttributes.put("dateofpicture", "14/12/1972");
            mediaAttributes.put("locationofpicture", "Halifax New");
            mediaAttributes.put("pictureakenby", "Mahima");
            mediaAttributes.put("locationofpicturetaken1", "Delhi");
            mediaAttributes.put("marriedto", "Udit");
            assertNotNull(genealogy.recordMediaAttributes(fileIdentifier, mediaAttributes));
        } catch (Exception ex) {

        }
    }

    @Test()
    void ShouldAddTagToMedia() {
        try {
            var fileIdentifier = genealogy.findMediaFile("CrapFile3.java");
            assertNotNull(fileIdentifier);

            assertNotNull(genealogy.tagMedia(fileIdentifier, "tag 1"));
        } catch (Exception ex) {

        }
    }

    @Test
    void ShouldAddPeopleInMedia() {
        try {
            var person1 = genealogy.findPerson("Madan lal Gandhi");
            var person2 = genealogy.findPerson("Vijay Gandhi");
            var person3 = genealogy.findPerson("Sudha Gandhi");
            var person4 = genealogy.findPerson("Udit Gandhi");
            List<PersonIdentity> persons = new ArrayList<>();
            persons.add(person1);
            persons.add(person2);
            persons.add(person3);
            persons.add(person4);

            var fileIdentifier = genealogy.findMediaFile("CrapFile.java");
            var result = genealogy.peopleInMedia(fileIdentifier, persons);

            fileIdentifier = genealogy.findMediaFile("CrapFile2.java");
             result = genealogy.peopleInMedia(fileIdentifier, persons);

            fileIdentifier = genealogy.findMediaFile("Crap3.java");
             result = genealogy.peopleInMedia(fileIdentifier, persons);

            //fileIdentifier = null;
            //fileIdentifier = genealogy.findMediaFile("CDE.java");
            //result = genealogy.peopleInMedia(fileIdentifier, persons);
        } catch (Exception ex) {

        }

    }

}
