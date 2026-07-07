package com.as.journal.service;

@FunctionalInterface
interface govtInstitution
{
    void department();

    default void work()
    {
        System.out.println("Manages govt. operations");
    }
}

public class test {
    public static void main(String [] args)
    {
        govtInstitution institution = new govtInstitution() {
            @Override
            public void department() {
                System.out.println("Financial Institution - RBI");
            }
        };

        institution.department();
        //institution.work();
    }
}
