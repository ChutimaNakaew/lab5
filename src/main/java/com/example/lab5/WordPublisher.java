package com.example.lab5;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    protected Word words;

    public WordPublisher() {
        words = new Word();
    }

    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
        words.badWords.add(s);
        return words.badWords;
    }

    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        words.badWords.remove(s);
        return words.badWords;
    }

    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
        words.goodWords.add(s);
        return words.goodWords;
    }

    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        words.goodWords.remove(s);
        return words.goodWords;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.GET)
    public void proofSentence(@PathVariable("sentence") String s) {
        for (int i = 0; i < words.goodWords.size(); i++) {
            boolean isFoundGood = words.goodWords.contains(s);
            if (isFoundGood) {
                rabbitTemplate.convertAndSend("myDirect", "good", s);
                System.out.println("Found Good Word");
            }
        }
        for (int i = 0; i < words.badWords.size(); i++) {
            boolean isFoundBad = words.badWords.contains(s);
            if (isFoundBad) {
                rabbitTemplate.convertAndSend("myDirect", "bad", s);
                System.out.println("Found Bad Word");
            }
        }


//        if (isFoundGood) {
//            rabbitTemplate.convertAndSend("myDirect", "good", s);
//            System.out.println("Found Good Word");
//        } else if (isFoundBad) {
//            rabbitTemplate.convertAndSend("myDirect", "bad", s);
//            System.out.println("Found Bad Word");
//        } else if (isFoundGood && isFoundBad) {
//            rabbitTemplate.convertAndSend("myFanout", "", s);
//            System.out.println("Found Good Word & Bad Word");
//        }
    }
}
