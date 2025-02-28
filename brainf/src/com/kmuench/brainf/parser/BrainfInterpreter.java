package com.kmuench.brainf.parser;

import java.io.IOException;
import java.util.Objects;

public class BrainfInterpreter {

    private static final int ENDE = -1;
    private static final int MEMORY_SIZE = 100;

    //
    // Variables for running
    //
    
    private int     m_lookahead;
    private int     m_functionPointer;
    private int     m_memoryPointer;
    private int[]  m_memory = null;
    private int[]   m_program = null;


    public BrainfInterpreter(String inputString) {
        Objects.requireNonNull(inputString);

        BrainfParser parser = new BrainfParser(inputString);
        this.m_program = parser.parse(); 

        reset();
    }

    public void     run() throws IOException {
        if (readyToRun() != true) throw new Error("Parser not reset");

        statement();

        reset();
    }

    public void     dumpProgram() {
        StringBuilder strb = new StringBuilder();
        StringBuilder tabs = new StringBuilder();

        for(int i=0;i<m_program.length;i++) {
            char c = (char) m_program[i];

            if (c == '+' || c == '-' || c == '.' || c == ',') 
            {
                strb.append(c);

                if (i+1 < m_program.length) 
                {
                    char nextChar = (char) m_program[i+1];
                    if (nextChar == '>' || nextChar == '<')
                    {
                        strb.append('\n').append(tabs);
                    }
                }
            }
            else if (c == '[') {
                strb.append("\n").append(tabs).append(c);
                tabs.append("\t");
                strb.append('\n').append(tabs);
            } else if (c == ']') {
                tabs.deleteCharAt(tabs.length()-1);
                strb.append("\n").append(tabs).append(c);
                strb.append('\n').append(tabs);

            }
            else strb.append(c);
        }

        System.out.println("Program code:");
        System.out.println(strb.toString());
    }

    private void    statement() throws IOException {
        boolean loop = true;
        while(m_functionPointer < m_program.length && loop) {
            switch (m_lookahead) {
                case '>': m_memoryPointer++;                                    match('>'); break;
                case '<': m_memoryPointer--;                                    match('<'); break;
                case '+': m_memory[m_memoryPointer]++;                          match('+'); break;
                case '-': m_memory[m_memoryPointer]--;                          match('-'); break;
                case '.': System.out.print((char)m_memory[m_memoryPointer]);    match('.'); break;
                case ',': m_memory[m_memoryPointer] = System.in.read();  match(','); break;
                case '[': brackets();                                                         break;
                default: loop = false;                                                        break;
            }
        }   

    }

    private void    brackets() throws IOException {
        int openedBracket = m_functionPointer;

        // If memory at current position is 0 jump forward behind closing ]
        if (m_memory[m_memoryPointer] == 0) {  
            int brackets = 1;

            for(;brackets > 0;m_functionPointer++) {

                // Check whether program has reached end, to throw error for missing closing bracket
                if (m_functionPointer >= m_program.length) throw new Error("Missing closing bracket ]");

                switch (m_program[m_functionPointer]) {
                    case '[': brackets++; break;
                    case ']': brackets--; break;
                }
            }
        }
        // Else continue executing next statement
        else {

            match('[');
            while(true) {
                statement();

                // If closing bracket
                match(']');
    
                if (m_memory[m_memoryPointer] != 0) {       // If memory at current position is 0 go back to opened bracket
                    m_functionPointer = openedBracket;      // Reset functionPointer to symbol following the opended [
                    nextInstruction();
                }
                else break;
            }
        }
    }

    private boolean readyToRun () {
        return m_memory != null && m_memoryPointer == 0 && m_functionPointer == 0;
    }

    private void    reset() {
        m_memory = new int[MEMORY_SIZE];
        m_memoryPointer = 0;
        m_functionPointer = -1;

        nextInstruction();
    }

    private void    nextInstruction() {
        while (true) {
            m_functionPointer++;
            if (m_functionPointer < m_program.length) 
            {
                m_lookahead = m_program[m_functionPointer];
            }
            else 
                m_lookahead = ENDE; 
            
            break;
        }
    } 

    private void    match(char t) {
        if (m_lookahead == (byte)t) 
        {   
            nextInstruction();
        }
        else throw new Error("syntax error"); 
    }
}
