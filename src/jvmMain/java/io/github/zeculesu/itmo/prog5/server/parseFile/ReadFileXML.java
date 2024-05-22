package io.github.zeculesu.itmo.prog5.server.parseFile;

import io.github.zeculesu.itmo.prog5.client.ElementFormConsole;
import io.github.zeculesu.itmo.prog5.data.*;
import io.github.zeculesu.itmo.prog5.error.FileCollectionException;
import io.github.zeculesu.itmo.prog5.error.IdException;
import io.github.zeculesu.itmo.prog5.error.InputFormException;
import io.github.zeculesu.itmo.prog5.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Парсинг из .xml
 */

public class ReadFileXML {

    public static void parseFile(String filePath, InMemorySpaceMarineCollection collection) throws FileNotFoundException, ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        AdvancedXMLHandler handler = new AdvancedXMLHandler(collection);
        try {
            parser.parse(new File(filePath), handler);

        } catch (SAXException | FileNotFoundException e) {
            throw new FileNotFoundException("Файл не найден");
        } catch (IOException e) {
            throw new FileNotFoundException("Проблема с чтением файла");
        }
    }

    private static class AdvancedXMLHandler extends DefaultHandler {
        InMemorySpaceMarineCollection collection;

        public AdvancedXMLHandler(InMemorySpaceMarineCollection collection) {
            this.collection = collection;
        }

        private HashMap<String, String> params = new HashMap<>();

        {
            clear_params();
        }

        private String lastElementName;

        public void clear_params() {
            this.params.put("name", null);
            this.params.put("x", null);
            this.params.put("y", null);
            this.params.put("creationDate", null);
            this.params.put("health", null);
            this.params.put("category", null);
            this.params.put("weaponType", null);
            this.params.put("meleeWeapon", null);
            this.params.put("chapter_name", null);
            this.params.put("parentLegion", null);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            this.lastElementName = qName;
            if (this.lastElementName.equals("element")) {
                this.params.put("id", attributes.getValue("id"));
            }
            if (this.lastElementName.equals("coordinates")) {
                this.params.put("x", attributes.getValue("x"));
                this.params.put("y", attributes.getValue("y"));
            }
            if (this.lastElementName.equals("chapter")) {
                this.params.put("chapter_name", attributes.getValue("name"));
                this.params.put("parentLegion", attributes.getValue("parentLegion"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                switch (this.lastElementName) {
                    case "name": {
                        this.params.put("name", information);
                        break;
                    }
                    case "creationDate": {
                        this.params.put("creationDate", information);
                        break;
                    }
                    case "health": {
                        this.params.put("health", information);
                        break;
                    }
                    case "category": {
                        this.params.put("category", information);
                        break;
                    }
                    case "weaponType": {
                        this.params.put("weaponType", information);
                        break;
                    }
                    case "meleeWeapon": {
                        this.params.put("meleeWeapon", information);
                        break;
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws InputFormException {
            ArrayList<String> keys = new ArrayList<>(this.params.keySet());
            ArrayList<String> valuesNull = new ArrayList<>();
            for (String key : keys) {
                if (this.params.get(key) == null) {
                    valuesNull.add(key);
                }
            }
            boolean fill = valuesNull.isEmpty();
            if (fill && qName.equals("element")) {
                try {
                    int id = ElementFormConsole.checkId(this.params.get("id"));
                    String name = ElementFormConsole.checkName(this.params.get("name"));
                    Coordinates coordinates = ElementFormConsole.checkCoordinates(this.params.get("x") + " " + this.params.get("y"));
                    int health = ElementFormConsole.checkHealth(this.params.get("health"));
                    AstartesCategory category = ElementFormConsole.checkCategory(this.params.get("category"));
                    Date creationDate = ElementFormConsole.checkCreationDate(this.params.get("creationDate"));
                    Weapon weaponType = ElementFormConsole.checkWeaponType(this.params.get("weaponType"));
                    MeleeWeapon meleeWeapon = ElementFormConsole.checkMeleeWeapon(this.params.get("meleeWeapon"));
                    Chapter chapter = ElementFormConsole.checkChapter(this.params.get("chapter_name") + " " + this.params.get("parentLegion"));
                    this.collection.add(id, new SpaceMarine(id, name, coordinates, creationDate, health,
                            category, weaponType, meleeWeapon, chapter));
                    clear_params();
                } catch (InputFormException | IdException e) {
                    System.out.println(e.getMessage());
                }
            } else if (!fill && qName.equals("element")) {
                System.out.println("Неправильный ввод параметров коллекции");
                for (String val : valuesNull) {
                    System.out.println("Отсутствует поле : " + val);
                }
            }
        }
    }
}
