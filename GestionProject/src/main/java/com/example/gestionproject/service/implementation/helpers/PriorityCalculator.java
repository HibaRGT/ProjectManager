package com.example.gestionproject.service.implementation.helpers;

import com.example.gestionproject.model.MoSCoWPriority;
import com.example.gestionproject.model.UserStory;
import org.springframework.stereotype.Component;

@Component
public class PriorityCalculator {

    public int calculateNote(UserStory s) {
        return (s.getValeurMetier() * 3)
                + (s.getUrgence() * 2)
                - s.getComplexite()
                - s.getRisques()
                + s.getDependances();
    }

    public MoSCoWPriority associatePriorite(int note) {
        if (note >= 18) return MoSCoWPriority.MUST_HAVE;
        if (note >= 13) return MoSCoWPriority.SHOULD_HAVE;
        if (note >= 8) return MoSCoWPriority.COULD_HAVE;
        return MoSCoWPriority.WONT_HAVE;
    }

    public void calculateAndSetPriority(UserStory us) {
        int note = calculateNote(us);
        us.setNotePriorite(note);
        us.setPriorite(associatePriorite(note));
    }
}
