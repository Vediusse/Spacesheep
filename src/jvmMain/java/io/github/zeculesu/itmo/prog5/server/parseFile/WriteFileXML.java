package io.github.zeculesu.itmo.prog5.server.parseFile;

import io.github.zeculesu.itmo.prog5.data.*;
import io.github.zeculesu.itmo.prog5.error.FileCollectionException;
import io.github.zeculesu.itmo.prog5.models.*;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Парсинг из .xml
 */

public class WriteFileXML {

    public static void writeFile(String filePath, SpaceMarineCollection collection) throws FileCollectionException {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(filePath), "UTF-8");
            writeCollection(writer, collection);
        } catch (FileNotFoundException e) {
            throw new FileCollectionException("Файл не найден");
        } catch (XMLStreamException e) {
            throw new FileCollectionException("Ошибка с файлом");
        } catch (NullPointerException e) {
            throw new FileCollectionException("Имя файла для записи не указано в переменной окружения FILENAME");
        } catch (Exception e) {
            throw new FileCollectionException("Непредвиденная ошибка при записи в файл");
        }
    }

    public static void writeCollection(XMLStreamWriter writer, SpaceMarineCollection collection) throws XMLStreamException {
        writer.writeStartDocument("UTF-8", "1.0");
        //todo может убрать строку ниже?
        writer.writeCharacters("\n");
        writer.writeStartElement("collection");
        for (SpaceMarine o : collection) {
            writer.writeCharacters("\n");

            writer.writeCharacters("\t");
            writer.writeStartElement("element");
            writer.writeAttribute("id", Integer.toString(o.getId()));
            writer.writeCharacters("\n");


            writer.writeCharacters("\t\t");
            writer.writeStartElement("name");
            writer.writeCharacters(o.getName());
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeEmptyElement("coordinates");
            writer.writeAttribute("x", Long.toString(o.getCoordinates().getX()));
            writer.writeAttribute("y", Float.toString(o.getCoordinates().getY()));
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeStartElement("creationDate");
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:ss");
            writer.writeCharacters(df.format(o.getCreationDate()));
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeStartElement("health");
            writer.writeCharacters(Integer.toString(o.getHealth()));
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeStartElement("category");
            writer.writeCharacters(o.getCategory().toString());
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeStartElement("weaponType");
            writer.writeCharacters(o.getWeaponType().toString());
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeStartElement("meleeWeapon");
            writer.writeCharacters(o.getMeleeWeapon().toString());
            writer.writeEndElement();
            writer.writeCharacters("\n");

            writer.writeCharacters("\t\t");
            writer.writeEmptyElement("chapter");
            writer.writeAttribute("name", o.getChapter().getName());
            writer.writeAttribute("parentLegion", o.getChapter().getParentLegion());
            writer.writeCharacters("\n");

            writer.writeCharacters("\t");
            writer.writeEndElement();

        }
        writer.writeEndElement();

        writer.writeEndDocument();
    }
}
