package com.demidovn.fruitbounty.game.services.game.missions.validators

import com.demidovn.fruitbounty.game.model.descriptors.MissionDescriptor
import com.demidovn.fruitbounty.game.model.descriptors.DescriptorType
import com.demidovn.fruitbounty.game.services.game.parsing.validators.DescriptorValidator
import spock.lang.Specification
import spock.lang.Subject

class MissionDescriptorValidatorTest extends Specification {

    @Subject
    DescriptorValidator validator = new DescriptorValidator()

    def "should nothing on correct data"() {
        given:
        MissionDescriptor missionDescriptor = new MissionDescriptor(
                DescriptorType.MISSION_STANDARD, null, null, false, 1, 0,
                false, 0, null, null)
        missionDescriptor.setVerb(verb)
        missionDescriptor.setVerbPositive(positiveVerb)
        missionDescriptor.setVerbNegative(negativeVerb)

        expect:
        validator.validVerbsFilling(missionDescriptor)

        where:
        verb  | positiveVerb  | negativeVerb
        "a"   | null          | null
        null  | "b"           | "c"
    }

    def "should throw exceptions on invalid data"() {
        given:
        MissionDescriptor missionDescriptor = new MissionDescriptor(
                DescriptorType.MISSION_STANDARD, null, null, false, 0,
                0, true, 0, null, null)
        missionDescriptor.setVerb(verb)
        missionDescriptor.setVerbPositive(positiveVerb)
        missionDescriptor.setVerbNegative(negativeVerb)

        when:
        validator.validVerbsFilling(missionDescriptor)

        then:
        def e = thrown(IllegalArgumentException)
        // e.message == var

        where:
        verb  | positiveVerb  | negativeVerb
        "a"   | "b"           | "c"
        "a"   | null          | "c"
        "a"   | "b"           | null

        null  | null          | "c"
        null  | "b"           | null

        null  | null          | null
        ""    | ""            | ""
        ""    | null          | null
    }

}
