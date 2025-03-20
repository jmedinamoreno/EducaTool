package com.medina.educatool.data.xml

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Entities
import com.fleeksoft.ksoup.parser.Parser
import com.medina.educatool.data.xml.model.MoodleQuestionnaire


class MoodleXML (){
    fun createXmlForQuestionary(questionnaire: MoodleQuestionnaire): String {
        // Create an empty XML document
        val doc: Document = Ksoup.parse(html = "", parser = Parser.xmlParser())

        // Create the root element
        val root: Element = doc.createElement("quiz")
        doc.appendChild(root)

        for (question in questionnaire.questions){
            if(question.summary.isEmpty() || question.responses.isEmpty()) continue

            val questionElement: Element = doc.createElement("question").attr("type", "multichoice")
            val summaryNameTextElement: Element = doc.createElement("text").text(question.summary)
            val summaryQuestionTextElement: Element = doc.createElement("text").text(question.summary)
            val nameElement: Element = doc.createElement("name").appendChild(summaryNameTextElement)
            val questiontextElement: Element = doc.createElement("questiontext")
                .attr("format", "plain_text")
                .appendChild(summaryQuestionTextElement)
            questionElement.appendChild(nameElement)
            questionElement.appendChild(questiontextElement)

            val numberOfCorrectResponses = question.responses.count { it.correct }
            val validPercent = 100 / numberOfCorrectResponses
            for (response in question.responses){
                val fraction = if(response.correct) validPercent.toString() else "0"
                val answerElement: Element = doc.createElement("answer").attr("fraction", fraction)
                val textElement: Element = doc.createElement("text").text(response.text)
                answerElement.appendChild(textElement)
                questionElement.appendChild(answerElement)
            }
            val singleElement: Element = doc.createElement("single").text("false")
            questionElement.appendChild(singleElement)
            root.appendChild(questionElement)
        }
        // Set the output settings for pretty-printing and XML escaping
        doc.outputSettings()
            .indentAmount(2)
            .prettyPrint(true)
            .syntax(Document.OutputSettings.Syntax.xml)
            .escapeMode(Entities.EscapeMode.base)

        // Return the XML as a string
        return "<?xml version=\"1.0\" ?>\n" + doc.outerHtml()
    }
}
