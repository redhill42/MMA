package euler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public final class Euler {
    private Euler() {}

    public static void main(String[] args) throws Exception {
        List<Integer> problems = null;
        Map<Integer, String> solutions = Solutions.getSolutions();
        List<String> results = new ArrayList<>();
        boolean timing = false;

        if (args.length > 0) {
            int i = 0;
            if ("-time".equals(args[0])) {
                timing = true;
                i++;
            }
            if (i < args.length) {
                problems = new ArrayList<>();
                for (; i < args.length; i++) {
                    problems.add(Integer.parseInt(args[i]));
                }
            }
        }
        if (problems == null) {
            problems = getProblems();
        }

        double totalTime = 0;
        for (Integer problem : problems) {
            try {
                long nano = System.nanoTime();
                String actual = run(problem);
                nano = System.nanoTime() - nano;

                double time = nano / 1e9;
                totalTime += time;

                if (timing) {
                    System.out.printf("%d. %-20s (%.6f)%n", problem, actual, time);
                } else {
                    System.out.printf("%d. %s%n", problem, actual);
                }

                String expected = solutions.get(problem);
                if (expected == null) {
                    results.add(format("Problem %d missing solution", problem));
                } else if (!expected.equals(actual)) {
                    results.add(format("Problem %d failed, expected: %s, but was %s", problem, expected, actual));
                } else if (time > 60.0) {
                    results.add(format("Problem %d failed, time out for 1 minute principal (%.4f)", problem, time));
                } else if (time > 5.0) {
                    results.add(format("Problem %d running time is too long (%.4f)", problem, time));
                }
            } catch (InvocationTargetException ex) {
                results.add(format("Problem %d failed with exception: %s", problem, ex.getCause()));
            } catch (Exception ex) {
                results.add(format("Problem %d failed with exception: %s", problem, ex));
            }
        }

        if (!results.isEmpty()) {
            System.out.println();
            results.forEach(System.out::println);
        }

        System.out.println();
        System.out.printf("total %dm%.4fs, average %.4fs%n",
                          (int)(totalTime/60), totalTime - (int)(totalTime/60) * 60,
                          totalTime / problems.size());
    }

    private static List<Integer> getProblems() throws ClassNotFoundException {
        Pattern pattern = Pattern.compile("Problem(\\d+)\\.class");
        return getProblemsForPackage("euler", pattern);
    }

    private static List<Integer> getProblemsForPackage(String pkgname, Pattern pattern)
        throws ClassNotFoundException
    {
        List<Integer> problems = new ArrayList<>();

        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if (cld == null) {
            throw new ClassNotFoundException("Can't get class loader.");
        }

        try {
            Enumeration<URL> resources = cld.getResources(pkgname.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                URLConnection connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    scanJarFile((JarURLConnection)connection, pkgname, pattern, problems);
                } else if ("file".equals(url.getProtocol())) {
                    File directory = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
                    scanDirectory(directory, pattern, problems);
                } else {
                    throw new ClassNotFoundException("Unsupported class loader.");
                }
            }
        } catch (IOException ex)  {
            throw new ClassNotFoundException("IOException occurred when trying to get resources.", ex);
        }

        problems.sort(Comparator.naturalOrder());
        return problems;
    }

    private static void scanDirectory(File directory, Pattern pattern, List<Integer> problems) {
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            if (files != null) {
                for (String file : files) {
                    Matcher m = pattern.matcher(file);
                    if (m.matches()) {
                        problems.add(Integer.parseInt(m.group(1)));
                    }
                }
            }
        }
    }

    private static void scanJarFile(JarURLConnection connection,
                                    String pkgname,
                                    Pattern pattern,
                                    List<Integer> problems)
        throws IOException
    {
        Enumeration<JarEntry> entries = connection.getJarFile().entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(pkgname + "/")) {
                name = name.substring(pkgname.length() + 1);
                Matcher matcher = pattern.matcher(name);
                if (matcher.matches()) {
                    problems.add(Integer.parseInt(matcher.group(1)));
                }
            }
        }
    }

    private static final String[] EMPTY_ARGS = new String[0];

    private static String run(int num) throws Exception {
        String className = "euler.Problem" + num;
        Class<?> clazz = Class.forName(className);
        Method main = clazz.getMethod("main", String[].class);

        PrintStream oldOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try {
            main.invoke(null, (Object)EMPTY_ARGS);
            return out.toString(Charset.defaultCharset().name()).trim();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } finally {
            System.setOut(oldOut);
        }
    }
}
