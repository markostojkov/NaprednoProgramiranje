package Labs2;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;


class ContactsTester {

    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}

enum Operator { VIP, ONE, TMOBILE }

abstract class Contact {
    private String date;

    Contact(String date) {
        this.date = date;
    }

    boolean isNewerThan(Contact c) throws ParseException {
        Date classDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.date);
        Date outsideDate = new SimpleDateFormat("yyyy-MM-dd").parse(c.date);

        return classDate.after(outsideDate);
    }

    abstract String getType();
}

class PhoneContact extends Contact {
    private String phone;

    PhoneContact(String date, String phone) {
        super(date);
        this.phone = phone;
    }

    String getPhone() {
        return this.phone;
    }

    Operator getOperator() {
        char operator = this.phone.charAt(2);

        switch (operator) {
            case '0':
            case '1':
            case '2':
            default:
                return Operator.TMOBILE;
            case '5':
            case '6':
                return Operator.ONE;
            case '7':
            case '8':
                return Operator.VIP;
        }
    }

    @Override
    String getType() {
        return this.getPhone();
    }
}

class EmailContact extends Contact {
    private String email;

    EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    String getEmail() {
        return this.email;
    }

    @Override
    String getType() {
        return this.getEmail();
    }
}

class Student {
    private List<Contact> contacts;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;

    Student (String firstName, String lastName, String city, int age, long index) {
        this.contacts = new ArrayList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
    }

    void addEmailContact(String date, String email) {
        this.contacts.add(new EmailContact(date, email));
    }

    void addPhoneContact(String date, String phone) {
        this.contacts.add(new PhoneContact(date, phone));
    }

    Contact[] getEmailContacts() {
        List<Contact> emailContacts = new ArrayList<>();

        for (Contact contact: this.contacts) {
            if (contact instanceof EmailContact) {
                emailContacts.add(contact);
            }
        }

        return emailContacts.isEmpty() ? new Contact[0] : (Contact[]) emailContacts.toArray();
    }

    Contact[] getPhoneContacts() {
        List<Contact> phoneContacts = new ArrayList<>();

        for (Contact contact: this.contacts) {
            if (contact instanceof PhoneContact) {
                phoneContacts.add(contact);
            }
        }

        return phoneContacts.isEmpty() ? new Contact[0] : (Contact[]) phoneContacts.toArray();
    }

    String getCity() {
        return this.city;
    }

    String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    long getIndex() {
        return this.index;
    }

    Contact getLatestContact() throws ParseException {
        Contact contact = this.contacts.get(0);

        for (Contact item: this.contacts) {
            if (!contact.isNewerThan(item)) {
                contact = item;
            }
        }

        return contact;
    }

    int getContactsNumber() {
        return this.contacts.size();
    }

    @Override
    public String toString() {
        return "";
        // return new Gson().toJson(this);
    }
}

class Faculty {
    private String name;
    private Student[] students;

    Faculty(String name, Student [] students) {
        this.name = name;
        this.students = students;
    }

    int countStudentsFromCity(String cityName) {
        return (int) Arrays.stream(this.students).filter(student -> student.getCity() == cityName).count();
    }

    Student getStudent(long index) {
        for (Student student: this.students) {
            if (student.getIndex() == index) return student;
        }

        return null;
    }

    double getAverageNumberOfContacts() {
        int contacts = 0;

        for (Student student: this.students) {
            contacts += student.getContactsNumber();
        }

        return contacts * 1.0 / Arrays.stream(this.students).count();
    }

    Student getStudentWithMostContacts() {
        Student student = this.students[0];

        for (Student s: this.students) {
            if (student.getContactsNumber() < s.getContactsNumber()) {
                student = s;
            }
        }

        return student;
    }

    @Override
    public String toString() {
        return "";
        //return new Gson().toJson(this);
    }
}

