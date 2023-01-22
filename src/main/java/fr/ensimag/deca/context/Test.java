package fr.ensimag.deca.context;
class Test{
    public static void main(String[] args) {
        EnvironmentType env = new EnvironmentType(null);
        ClassType obj = env.OBJECT;
        if (obj.isSubClassOf(obj)){
            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh");
        }
    }
}