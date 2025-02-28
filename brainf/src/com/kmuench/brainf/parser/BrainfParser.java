package com.kmuench.brainf.parser;

import java.util.Arrays;
import java.util.Objects;

public class BrainfParser {

    private static final int EOF = -1;

    //
    // Variables for parsing
    //
    
    private int     m_lookahead = EOF;
    private int     m_bufferPos = -1;
    private int     m_codePos = -1;
    private String  m_buffer;
    private int[]  m_code = null;
    


    
    public BrainfParser(String m_buffer) {
        Objects.requireNonNull(m_buffer);
        this.m_buffer = m_buffer;

        reset();
    }

    public int[]   parse() {
        if (!readyToRun()) throw new Error("Parser not initzialized");

        statement();

        if (m_lookahead != EOF) throw new Error("syntax error, unused symbols");
        
        int size = 0;
        for(;size<m_code.length;size++) if(m_code[size] == -1) break;

        int[] retCode = new int[size];
        for(int i=0;i<size;i++) retCode[i] = m_code[i];
        
        reset();

        return retCode;
    }

    private void    statement() {
        boolean loop = true;
        while(m_lookahead != EOF && loop) {
            switch (m_lookahead) {
                case '>': match('>'); break;
                case '<': match('<'); break;
                case '+': match('+'); break;
                case '-': match('-'); break;
                case '.': match('.'); break;
                case ',': match(','); break;
                case '[': brackets();   break;
                default:  loop = false; break;  // epsilon
            }
        }
    }

    private void    brackets() {
        match('['); statement(); match(']');
    }

    private void    match(int t) {
        if (m_lookahead == t) 
        {   
            m_code[m_codePos++] = (byte) m_lookahead;
            nextChar();
        }
        else throw new Error("syntax error"); 
    }

    private void    nextChar() {
        while (true) {
            m_bufferPos++;
            if (m_bufferPos < m_buffer.length()) 
            {
                char nextChar = m_buffer.charAt(m_bufferPos);
                if (!isValid(nextChar))  continue;

                m_lookahead = nextChar;
            }
            else 
                m_lookahead = EOF; 
                
            break;
        }
    } 

    private boolean readyToRun() {
        boolean reset =  m_bufferPos == 0 && m_codePos == 0 && m_code != null;

        for(int i=0;i<m_code.length;i++) if (m_code[i] != -1) return false;

        return reset;
    }

    private void    reset() {
        m_lookahead = EOF;
        m_bufferPos = -1;
        m_codePos = 0;
        m_code = new int[m_buffer.length()];
        Arrays.fill(m_code, (byte) -1);

        if (m_buffer.length() > 0) nextChar();
        else m_bufferPos = 0;
    }

    private static boolean isValid(char c) {
        return c == '>' || c == '<' || c == '+' || c == '-' || c == '.' || c == ',' || c == '[' || c == ']';
    }

}
