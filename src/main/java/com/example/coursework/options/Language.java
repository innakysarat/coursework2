package com.example.coursework.options;

public class Language {

    private String language;
    public Language(String language){
        this.language = language;
    }

  /*  @OneToMany(mappedBy = "language", fetch = FetchType.EAGER)
    private Set<Internship> internships = new HashSet<>();
    public void addInternship(Internship internship){
        internships.add(internship);
    }*/
}
