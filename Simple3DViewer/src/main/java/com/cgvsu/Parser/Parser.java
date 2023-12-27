package com.cgvsu.Parser;

import com.cgvsu.GuiController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static List<Integer> parsing(String input) {
        if (input.length() == 1) {
            List<Integer> result = new ArrayList<>();
            result.add(Integer.parseInt(input));
            return result;
        } else if (input.contains("-")) {
            String[] parts = input.split("-");
            int start, end;
            try {
                start = Integer.parseInt(parts[0]);
                end = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                GuiController.exception("Error: Invalid input format");
                return null;
            }

            List<Integer> result = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                result.add(i);
            }

            return result;
        } else {
            String[] parts = input.split(", ");
            List<Integer> result = new ArrayList<>();

            for (String part : parts) {
                try {
                    result.add(Integer.parseInt(part));
                } catch (NumberFormatException e) {
                    GuiController.exception("Error: Invalid input format");
                    return null;
                }
            }

            return result;
        }
    }
}