//todo how to get thymeleaf data from javascript for bin rages

//Constants
const DEFAULT_CARD_NUMBER = '.... .. .....';
const AMEX_SCHEME = 'amex';
const DINERS_SCHEME = 'diners';
const VISA_SCHEME = 'visa';
const MASTER_SCHEME = 'master';
const DEFAULT_MASKED_CARD_NUMBER = '.... .... .... ....';
const INVALID_CARD_NUMBER = "Card number is invalid";
const INVALID_ACCOUNT_NUMBER = "Account number is invalid";
const INVALID_EXPIRY_DATE = "Invalid expiry date";
const EXPIRY_MONTH_REQUIRED = "Please select your expiry month.";
const EXPIRY_YEAR_REQUIRED = "Please select your expiry year.";
const DEFAULT_CARD_SCHEME = 'unselected';
const MAESTRO_MASTER_CARD = 'maestro';
const DEFAULT_EXPIRY = '..';


// application State
let State = {
    cardNumber: DEFAULT_CARD_NUMBER,
    previousCardScheme: DEFAULT_CARD_SCHEME,
    cardScheme: DEFAULT_CARD_SCHEME,
    cardNumberMaxLength: 19,
    accountNumber: '',
    expiryMonth: DEFAULT_EXPIRY,
    expiryYear: DEFAULT_EXPIRY,
    isNorwegianCard: false,
    cardSubType: '',
    cardNumberHasError: true,
    expiryMonthHasError: true,
    expiryYearHasError: true,
    accountNumberHasError: false,
}

//accessor function
let setCardNumberError = (isError, message) => {
    State.cardNumberHasError = isError
    updateFeedbackField(isError, cardNumberFeedback, message)
}
let setExpiryMonthError = (isError, message) => {
    State.expiryMonthHasError = isError
    updateFeedbackField(isError, expiryDateFeedback, message)
}
let setExpiryYearError = (isError, message) => {
    State.expiryYearHasError = isError
    updateFeedbackField(isError, expiryDateFeedback, message)
}
let setAccountNumberError = (isError, message) => {
    State.accountNumberHasError = isError
    updateFeedbackField(isError, accountNumberFeedback, message)
}
let setIsNorwegianCard = (isNorwegian) => {
    if (isNorwegian) {
        setAccountNumberError(true, INVALID_ACCOUNT_NUMBER)
        accountNumberField.required = true
    } else {
        setAccountNumberError(false, INVALID_ACCOUNT_NUMBER)
        accountNumberField.required = false
    }
    State.isNorwegianCard = isNorwegian
}
let bankAxeptCardNumberCheck = () => {
    // todo implement bin range check and update app state
    setIsNorwegianCard(false)
}

//DOM references
const cardBannerAccountNoLabel = document.getElementById("cardAccountNoLabel")
const maskedAccountNumberField = document.getElementById("maskedAccountNumberField")
const bankAxeptLogo = document.getElementById("bankAxeptLogo")
const maskedCardNumberField = document.getElementById("maskedCardNumberField")
const cardBannerExpiryMonth = document.getElementById("cardBannerExpiryMonth")
const cardBannerExpiryYear = document.getElementById("cardBannerExpiryYear")
const cardBannerSchemeLogo = document.getElementById("cardBannerSchemeLogo")
const bankCard = document.getElementById("bankCard")

const cardNumberField = document.getElementById("cardNumberField")
const cardNumberSchemeLogo = document.getElementById("cardNumberSchemeLogo")
const cardNumberFeedback = document.getElementById("cardNumberFeedback")

const expiryMonthField = document.getElementById("expiryMonthField")
const expiryYearField = document.getElementById("expiryYearField")
const expiryDateFeedback = document.getElementById("expiryDateFeedback")

const accountNumberFieldContainer = document.getElementById("accountNumberFieldContainer")
const accountNumberField = document.getElementById("accountNumberField")
const accountNumberFeedback = document.getElementById("accountNumberFeedback")

const saveButton = document.getElementById("saveButton")
const responseMessage = document.getElementById("responseJsonString")

//helper functions
const maskAccountNumber = (accountNumber) => {
    if (accountNumber !== '' && accountNumber !== undefined) {
        const result = value.replace(/.(?=.{5})/g, '*')
        return maskNumber(result, 4, 6, 11)
    }
    return DEFAULT_CARD_NUMBER
}

const maskLastFourDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')
    const addSpace = result.match(/.{1,4}/g)
    return addSpace?.join(' ')
}

const maskNumber = (result, b1End, b2End, b3End) => {
    const b1 = result.substring(0, b1End)
    const b2 = result.substring(b1End, b2End)
    const b3 = result.substring(b2End, b3End)
    return `${b1} ${b2} ${b3}`
}

const maskCardNumberByScheme = () => {
    const cardNumber = State.cardNumber
    const cardScheme = State.cardScheme

    if (cardNumber !== undefined) {
        switch (cardScheme) {
            case AMEX_SCHEME:
                const result = cardNumber.replace(/.(?=.{4})/g, '*')
                return maskNumber(result, 4, 10, 15)
            case DINERS_SCHEME:
                if (cardNumber && cardNumber.length === 16) {
                    return maskLastFourDigit(cardNumber)
                }
                if (cardNumber && cardNumber.length === 14) {
                    const result = cardNumber.replace(/.(?=.{4})/g, '*')
                    return maskNumber(result, 4, 10, 14)
                }
                break
            case VISA_SCHEME:
                return maskLastFourDigit(cardNumber)
            case MASTER_SCHEME:
                return maskLastFourDigit(cardNumber)
        }
    }

    if (cardNumber === '') {
        return DEFAULT_MASKED_CARD_NUMBER
    }

    return maskLastFourDigit(cardNumber)
}

const luhnsAlgoCheck = ((arr) => {
    return (cardNumber) => {
        if (!cardNumber) {
            return false
        }
        let len = cardNumber.length
        let bit = 1
        let sum = 0
        let val

        while (len) {
            val = parseInt(cardNumber.charAt(--len), 10)
            sum += (bit ^= 1) ? arr[val] : val
        }
        return sum % 10 === 0
    }
})([0, 2, 4, 6, 8, 1, 3, 5, 7, 9])

const firstDigitCheck = (cardNumber) => {
    if (!cardNumber) {
        return false
    }

    return !!cardNumber.match(/^[2,3,4,5,6]/);
}

const cardSchemeLengthOk = (cardNumber, cardScheme) => {
    if (!cardNumber) {
        return false
    }

    let checkCardSchemeLength = false

    if (cardNumber.length < 12) {
        return false
    }

    if (cardScheme === VISA_SCHEME) {
        checkCardSchemeLength = cardNumber.length === 16;
    }
    if (cardScheme === MASTER_SCHEME) {
        checkCardSchemeLength = cardNumber.length === 16;
        if (State.cardSubType === MAESTRO_MASTER_CARD) {
            checkCardSchemeLength = cardNumber.length >= 12 && cardNumber.length <= 19;
        }
    }

    if (cardScheme === AMEX_SCHEME) {
        checkCardSchemeLength = cardNumber.length === 15;
    }
    if (cardScheme === DINERS_SCHEME) {
        checkCardSchemeLength = cardNumber.length >= 14 && cardNumber.length <= 16;
    }
    State.cardSubType = ''
    return checkCardSchemeLength
}

const mod11AlgoCheck = ((weights) => {
    return (value) => {
        if (!value) {
            return false
        }

        let i = value.length - 1
        let sum = 0
        let val

        const checkNumber = Number.parseInt(value.charAt(10), 10)
        const accountNumberWithoutCheckNumber = value.substring(0, 10)

        while (i) {
            val = parseInt(accountNumberWithoutCheckNumber.charAt(--i), 10)
            sum += val * weights[i]
        }

        const remainder = sum % 11
        const mode11CheckNumber = remainder === 0 ? 0 : 11 - remainder
        return mode11CheckNumber === checkNumber
    }
})([5, 4, 3, 2, 7, 6, 5, 4, 3, 2])

const cardNumberCheck = () => {
    const cardNumber = State.cardNumber
    if (cardNumber) {
        const isValid = luhnsAlgoCheck(cardNumber) && firstDigitCheck(cardNumber) && cardSchemeLengthOk(cardNumber, State.cardScheme)
        setCardNumberError(!isValid, INVALID_CARD_NUMBER)
    }
}

const expiryDateCheck = () => {
    if (State.expiryYear !== DEFAULT_EXPIRY) {
        const expiryYear = parseInt(State.expiryYear, 10)
        const expiryMonth = parseInt(State.expiryMonth, 10)
        const cardExpiryDate = new Date(expiryYear + 2000, expiryMonth - 1)
        const today = new Date()

        if (cardExpiryDate.getFullYear() < today.getFullYear()) {
            setExpiryYearError(true, INVALID_EXPIRY_DATE)
        } else if (cardExpiryDate.getFullYear() === today.getFullYear()) {
            if (cardExpiryDate.getMonth() < today.getMonth()) {
                setExpiryMonthError(true, INVALID_EXPIRY_DATE)
            }
        } else {
            setExpiryYearError(false, INVALID_EXPIRY_DATE)
            setExpiryMonthError(false, INVALID_EXPIRY_DATE)
        }
    }
}

const accountNumberCheck = () => {
    const accountNumber = State.accountNumber
    if (accountNumber) {
        const isValid = mod11AlgoCheck(accountNumber)
        setAccountNumberError(!isValid, INVALID_ACCOUNT_NUMBER)
    }
}

const cardNumberMaxLengthByScheme = () => {
    const cardScheme = State.cardScheme
    const cardNumber = State.cardNumber
    if (cardScheme === VISA_SCHEME) {
        return 16
    }
    if (cardScheme === MASTER_SCHEME) {
        const valueStripped = cardNumber.replace(/\D/g, '') // Stripp everything but numbers
        const isMaestroCard = valueStripped.match(/^(?:50[0-9]|5[6-8]|6[0-4]|67[0-9]|69[0-9])\d+$/)
        if (isMaestroCard) {
            return 19
        }
        return 16
    }
    if (cardScheme === AMEX_SCHEME) {
        return 15
    }
    if (cardScheme === DINERS_SCHEME) {
        return 16
    }
    return 19
}

const schemeFromCardNumber = () => {
    const cardNumber = State.cardNumber
    let scheme = State.cardScheme
    State.previousCardScheme = scheme
    if (!cardNumber || cardNumber.length < 2) {
        scheme = DEFAULT_CARD_SCHEME
    }

    const valueStripped = cardNumber.replace(/\D/g, '') // Stripp everything but numbers  // old code (/[^0-9. ]\s/g, "");

    if (valueStripped.match(/^4[0-9]+$/)) {
        scheme = VISA_SCHEME
    }
    if (valueStripped.match(/^5[1-5][0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^222[1-9][0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^22[3-9][0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^2[3-6][0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^27[01][0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^2720[0-9]+$/)) {
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^(?:50[0-9]|5[6-8]|6[0-4]|67[0-9]|69[0-9])\d+$/)) {
        State.cardSubType = MAESTRO_MASTER_CARD
        scheme = MASTER_SCHEME
    }
    if (valueStripped.match(/^3[47][0-9]+$/)) {
        scheme = AMEX_SCHEME
    }
    if (valueStripped.match(/^3(?:0[0-5]|09|[68][0-9])[0-9]+$/)) {
        scheme = DINERS_SCHEME
    }
    State.cardScheme = scheme
}

//DOM update functions
const updateMaskedAccountNumber = () => {
    if (State.isNorwegianCard) {
        bankAxeptLogo.classList.toggle("hidden", !State.isNorwegianCard)
        cardBannerAccountNoLabel.classList.toggle("hidden", !State.isNorwegianCard)
        accountNumberFieldContainer.classList.toggle("hidden", !State.isNorwegianCard)
        maskedAccountNumberField.classList.toggle("hidden", !State.isNorwegianCard)
        maskedAccountNumberField.textContent = maskAccountNumber(State.accountNumber)
    }
}

const updateExpiryDate = () => {
    cardBannerExpiryMonth.textContent = State.expiryMonth
    cardBannerExpiryYear.textContent = State.expiryYear
}

const updateCardBannerComponents = () => {
    updateMaskedCardNumberField()
    updateSchemeClassOn(cardBannerSchemeLogo)
    updateSchemeClassOn(bankCard)
    updateSchemeClassOn(cardNumberSchemeLogo)
    updateExpiryDate();
    updateMaskedAccountNumber();
}

const updateMaskedCardNumberField = () => {
    maskedCardNumberField.textContent = maskCardNumberByScheme(State.cardNumber, State.cardScheme)
}

const updateFeedbackField = (isError, feedbackField, message) => {
    if (isError) {
        feedbackField.textContent = message
    } else {
        feedbackField.textContent = ""
    }

};

const updateSchemeClassOn = (component) => {
    component.classList.remove(State.previousCardScheme)
    component.classList.add(State.cardScheme)
};

const updateCardNumberMaxLength = () => {
    const cardMaxLength = cardNumberMaxLengthByScheme(State.cardNumber, State.cardScheme)
    cardNumberField.maxLength = cardMaxLength
    State.cardNumberMaxLength = cardMaxLength
}

const removeNonNumbersFromCardNumberField = (e) => {
    e.target.value = e.target.value.replace(/\D/g, '')
    if (e.target.value !== State.cardNumber) {
        State.cardNumber = e.target.value
    }
}

const removeNonNumbersFromAccountNoField = (e) => {
    e.target.value = e.target.value.replace(/\D/g, '')
    if (e.target.value !== State.accountNumber) {
        State.accountNumber = e.target.value
        setAccountNumberError(false, INVALID_ACCOUNT_NUMBER)
    }
}

const updateSaveButton = () => {
    if (State.cardNumberHasError || State.expiryMonthHasError || State.expiryYearHasError || State.accountNumberHasError) {
        saveButton.disabled = true
        saveButton.classList.remove("saveButton")
    } else {
        saveButton.disabled = false
        saveButton.classList.add("saveButton")
    }
}


//Event handlers
const processCardNumberField = (e) => {
    removeNonNumbersFromCardNumberField(e)
    schemeFromCardNumber()
    updateCardNumberMaxLength()
}

const cardNumberOnBlurHandler = (e) => {
    processCardNumberField(e);
    cardNumberCheck();
    updateCardBannerComponents()
    bankAxeptCardNumberCheck()
    updateSaveButton()
}

const cardNumberOnKeyupHandler = (e) => {
    processCardNumberField(e);
    updateCardBannerComponents()
    if (e.target.value.length === State.cardNumberMaxLength) {
        cardNumberCheck()
        updateCardBannerComponents()
        bankAxeptCardNumberCheck()
    } else {
        State.cardNumberHasError = true
    }
    updateSaveButton()
}

const expiryMonthOnChangeHandler = (e) => {
    const value = e.target.value;
    if (!value || isNaN(value)) {
        setExpiryMonthError(true, EXPIRY_MONTH_REQUIRED)
    } else {
        State.expiryMonth = value
        updateCardBannerComponents()
        setExpiryMonthError(false, EXPIRY_MONTH_REQUIRED)
    }
    expiryDateCheck()
    updateSaveButton()
}

const expiryYearOnChangeHandler = (e) => {
    const value = e.target.value;
    if (!value || isNaN(value)) {
        setExpiryYearError(true, EXPIRY_YEAR_REQUIRED)
    } else {
        State.expiryYear = value
        updateCardBannerComponents()
        setExpiryYearError(false, EXPIRY_YEAR_REQUIRED)
    }
    expiryDateCheck()
    updateSaveButton()
}

const accountNumberOnKeyupHandler = (e) => {
    removeNonNumbersFromAccountNoField(e)
    updateCardBannerComponents()
    if (e.target.value.length === 11) {
        accountNumberCheck()
    } else {
        State.accountNumberHasError = true
    }
    updateSaveButton()
}

const accountNumberOnBlurHandler = (e) => {
    removeNonNumbersFromAccountNoField(e)
    updateCardBannerComponents()
    accountNumberCheck()
    updateFeedbackField(accountNumberFeedback, INVALID_CARD_NUMBER);
    updateSaveButton()
}

const postMessageToParent = (response) => {
    window.parent.postMessage(
        response
        , "*" //Todo replace with flytoget domain
    )
}

//Event handler bindings
cardNumberField.onblur = e => cardNumberOnBlurHandler(e)
cardNumberField.onkeyup = e => cardNumberOnKeyupHandler(e)
expiryMonthField.onchange = e => expiryMonthOnChangeHandler(e)
expiryYearField.onchange = e => expiryYearOnChangeHandler(e)
accountNumberField.onkeyup = e => accountNumberOnKeyupHandler(e)
accountNumberField.onblur = e => accountNumberOnBlurHandler(e)


//page setup
cardNumberField.setAttribute("maxLength", cardNumberMaxLengthByScheme(State.cardNumber, State.cardScheme).toString())
if (responseMessage != null) {
    const response = responseMessage.textContent
    responseMessage.remove()
    const parsedJson = JSON.parse(response)
    let jsonResponse = {
        event_id: "flytoget_id",
    };
    jsonResponse.data = parsedJson
    postMessageToParent(jsonResponse)
}