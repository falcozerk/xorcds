package com.falcozerk.xorcds;

public class XorCds {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Syntax: XorCds sourcePath targetPath");
        }

        App app = new App(args[0], args[1]);
        app.run();
    }
}
