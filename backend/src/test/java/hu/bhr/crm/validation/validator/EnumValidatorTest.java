package hu.bhr.crm.validation.validator;

import hu.bhr.crm.validation.annotation.ValidEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnumValidatorTest {

    @Mock
    private ValidEnum annotation;
    private EnumValidator underTest;

    private enum TestEnum {
        VALUE_ONE,
        VALUE_TWO,
        VALUE_THREE
    }

    @BeforeEach
    void setUp() {
        underTest = new EnumValidator();
        when(annotation.enumClass()).thenReturn((Class) TestEnum.class);
        underTest.initialize(annotation);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "VALUE_ONE",
            "value_one",
            "Value_One",
            "vAlUe_oNE",
            " VALUE_ONE ",
            " VALUE _ ONE ",
            "VALUE - _ ONE",
            "VALUE-TWO",
            "value-two",
            "Value-Two",
            " VALUE-TWO ",
            " VALUE - TWO ",
            "VALUE--TWO",
            " VALUE - - TWO ",
            "VALUE THREE",
            "value three",
            "Value Three",
            " VALUE THREE "
    })
    void shouldReturnTrueForValidEnumValues(String value) {
        assertTrue(underTest.isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "INVALID_VALUE",
            "VALUEONE",
            "VALUEFOUR",
            "VAL_UE_ONE",
            " _VALUE__ONE_ ",
            "VALUE@TWO",
            "123",
            "VALUE#THREE"
    })
    void shouldReturnFalseForInvalidEnumValues(String value) {
        assertFalse(underTest.isValid(value, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ",
            "",
             " " ,
            "\t",
            "\n"
    })
    void shouldReturnTrueForBlankValues(String value) {
        assertTrue(underTest.isValid(value, null));
    }

    @Test
    void shouldReturnTrueForNullValue() {
        assertTrue(underTest.isValid(null, null));
    }
}