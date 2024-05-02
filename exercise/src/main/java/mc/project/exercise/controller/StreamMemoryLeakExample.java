package mc.project.exercise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Stream;

@RestController
@RequestMapping("/exercise1")
public class StreamMemoryLeakExample {

    @GetMapping
    public void test(String[] args) {
        try {
            Stream<String> lines = readLinesFromFile("C:\\projects\\Spring\\exercise\\src\\main\\resources\\streamMemoryLeakFile.txt");
            lines.forEach(System.out::println);
            //lines.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Stream<String> readLinesFromFile(String fileName) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        return reader.lines();
    }
}
