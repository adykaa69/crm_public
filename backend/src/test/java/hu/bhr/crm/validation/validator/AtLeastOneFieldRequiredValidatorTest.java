package hu.bhr.crm.validation.validator;

import hu.bhr.crm.validation.annotation.AtLeastOneFieldRequired;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtLeastOneFieldRequiredValidatorTest {

    private AtLeastOneFieldRequiredValidator underTest;

    @Mock
    private AtLeastOneFieldRequired annotation;

    @BeforeEach
    void setUp() {
        underTest = new AtLeastOneFieldRequiredValidator();

        when(annotation.message()).thenReturn("At least one field must be provided.");
        when(annotation.fields()).thenReturn(new String[]{"field1", "field2", "field3"});

        underTest.initialize(annotation);
    }

    record TestDto(
            String field1,
            String field2,
            String field3
    ) {}

    static TestDto[] dtosWithAtLeastOneField = {
            new TestDto("value", null, null),
            new TestDto(null, "value", null),
            new TestDto(null, null, "value"),
            new TestDto("value", "value", null),
            new TestDto("value", null, "value"),
            new TestDto(null, "value", "value"),
            new TestDto("value", "value", "value")
    };

    static TestDto[] dtosWithNoFields = {
            new TestDto(null, null, null),
            new TestDto("", "", ""),
            new TestDto(" ", " ", " "),
            new TestDto("\t", "\t", "\t"),
            new TestDto("\n", "\n", "\n")
    };

    @ParameterizedTest
    @FieldSource("dtosWithAtLeastOneField")
    void shouldReturnTrueWhenAtLeastOneFieldIsProvided(TestDto dto) {
        assertTrue(underTest.isValid(dto, null));
    }

    @ParameterizedTest
    @FieldSource("dtosWithNoFields")
    void shouldReturnFalseWhenNoFieldsAreProvided(TestDto dto,
                                                  @Mock ConstraintValidatorContext context,
                                                  @Mock ConstraintValidatorContext.ConstraintViolationBuilder builder) {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);

        assertFalse(underTest.isValid(dto, context));
    }
}