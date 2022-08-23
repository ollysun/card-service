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

    printState: function () {
        const message = "CardNumber : " + this.cardNumber
            + " previousCardScheme : " + this.previousCardScheme
            + " CardScheme : " + this.cardScheme
            + " cardNumberMaxLength : " + this.cardNumberMaxLength
            + " accountNumber : " + this.accountNumber
            + " isNorwegianCard : " + this.isNorwegianCard
            + " cardSubType : " + this.cardSubType
            + " cardNumberHasError : " + this.cardNumberHasError
            + " expiryMonthHasError : " + this.expiryMonthHasError
            + " expiryYearHasError : " + this.expiryYearHasError
            + " accountNumberHasError : " + this.accountNumberHasError

        console.log(message)
    }
}

//accessor function
let setCardNumberError = (isError) => State.cardNumberHasError = isError
let setMonthExpiryError = (isError) => State.expiryMonthHasError = isError
let setExpiryYearError = (isError) => State.expiryYearHasError = isError
let setAccountNumberError = (isError) => State.expiryYearHasError = isError
let setIsNorwegianCard = (isNorwegian) => {
    if (isNorwegian) {
        setAccountNumberError(true)
    } else {
        setAccountNumberError(false)
    }
    State.isNorwegianCard = isNorwegian
}
let bankAxeptCardNumberCheck = () => {
    // todo implement bin range check and update app state
    State.isNorwegianCard = false
}


//DOM element references
const cardBannerAccountNoLabel = document.getElementById("cardAccountNoLabel")
const cardAccountNumber = document.getElementById("cardAccountNumber")
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

//helper functions
const maskAccountNumber = (accountNumber) => {
    if (accountNumber !== '' && accountNumber !== undefined) {
        return maskDebitCardDigit(accountNumber ?? '')
    }
    return DEFAULT_CARD_NUMBER
}

const maskLastFourDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')
    const addSpace = result.match(/.{1,4}/g)
    return addSpace?.join(' ')
}

const maskDinersFourWithFourteenDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 10)
    const b3 = result.substring(10, 14)
    return `${b1} ${b2} ${b3}`
}

const maskAmexFourDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 10)
    const b3 = result.substring(10, 15)
    return `${b1} ${b2} ${b3}`
}

const maskDebitCardDigit = value => {
    const result = value.replace(/.(?=.{5})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 6)
    const b3 = result.substring(6, 11)
    return `${b1} ${b2} ${b3}`
}

const maskCardNumberByScheme = () => {
    const cardNumber = State.cardNumber
    const cardScheme = State.cardScheme

    if (cardNumber !== undefined && cardScheme === AMEX_SCHEME) {
        return maskAmexFourDigit(cardNumber)
    }
    if (cardNumber !== undefined && cardScheme === DINERS_SCHEME) {
        if (cardNumber !== '' && cardNumber.length === 16) {
            return maskLastFourDigit(cardNumber)
        }
        if (cardNumber !== '' && cardNumber.length === 14) {
            return maskDinersFourWithFourteenDigit(cardNumber)
        }
    }
    if (cardNumber !== undefined && cardScheme === VISA_SCHEME) {
        return maskLastFourDigit(cardNumber)
    }
    if (cardNumber !== undefined && cardScheme === MASTER_SCHEME) {
        return maskLastFourDigit(cardNumber)
    }

    if (cardNumber === '' || cardNumber === undefined) {
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
    const isValid = luhnsAlgoCheck(cardNumber) && firstDigitCheck(cardNumber) && cardSchemeLengthOk(cardNumber, State.cardScheme)
    setCardNumberError(!isValid)
}
const isExpiryDateValid = () => {
    const expiryYear = parseInt(State.expiryYear, 10)
    const expiryMonth = parseInt(State.expiryMonth, 10)
    const cardExpiryDate = new Date(expiryYear + 2000, expiryMonth - 1)
    const today = new Date()

    if (cardExpiryDate.getFullYear() < today.getFullYear()) {
        return true
    }

    if (cardExpiryDate.getFullYear() === today.getFullYear()) {
        if (cardExpiryDate.getMonth() < today.getMonth()) {
            return true
        }
    }

    return false
}
const expiryCardCheck = () => {
    const isValid = isExpiryDateValid()
    setExpiryYearError(!isValid)
}

const accountNumberCheck = () => {
    const accountNumber = State.accountNumber
    const isValid = mod11AlgoCheck(accountNumber)
    setAccountNumberError(!isValid)
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
const updateBannerAccountNumber = () => {
    if (State.isNorwegianCard) {
        bankAxeptLogo.classList.toggle("hidden", !State.isNorwegianCard)
        cardBannerAccountNoLabel.classList.toggle("hidden", !State.isNorwegianCard)
        accountNumberFieldContainer.classList.toggle("hidden", !State.isNorwegianCard)
        cardAccountNumber.classList.toggle("hidden", !State.isNorwegianCard)
        cardAccountNumber.textContent = State.accountNumber
    }
}

const updateExpiryDate = () => {
    cardBannerExpiryMonth.textContent = State.expiryMonth
    cardBannerExpiryYear.textContent = State.expiryYear
}

const updateCardBannerComponents = () => {
    updateMaskedCardNumberFieldLength()
    updateSchemeClassOn(cardBannerSchemeLogo)
    updateSchemeClassOn(bankCard)
    updateSchemeClassOn(cardNumberSchemeLogo)
    updateExpiryDate();
    updateBannerAccountNumber();
}

const updateMaskedCardNumberFieldLength = () => {
    maskedCardNumberField.textContent = maskCardNumberByScheme(State.cardNumber, State.cardScheme)
}

const updateFeedbackField = (feedbackField, message) => {
    if (State.cardNumberHasError) {
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
        setCardNumberError(false)
        updateFeedbackField(cardNumberFeedback, INVALID_CARD_NUMBER)
    }
}

const removeNonNumbersFromAccountNoField = (e) => {
    e.target.value = e.target.value.replace(/\D/g, '')
    if (e.target.value !== State.accountNumber) {
        State.accountNumber = e.target.value
        setAccountNumberError(false)
        updateFeedbackField(accountNumberFeedback, INVALID_ACCOUNT_NUMBER)
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
}

const cardNumberOnChangeHandler = (e) => {
    processCardNumberField(e);
    cardNumberCheck();
    updateFeedbackField(cardNumberFeedback, INVALID_CARD_NUMBER);
    updateSaveButton()
    updateCardNumberMaxLength()
    updateCardBannerComponents()
    bankAxeptCardNumberCheck()
}

const cardNumberOnKeyupHandler = (e) => {
    processCardNumberField(e);
    updateCardNumberMaxLength()
    updateCardBannerComponents()
    if (e.target.value.length === State.cardNumberMaxLength) {
        cardNumberCheck()
        updateCardBannerComponents()
        updateFeedbackField(cardNumberFeedback, INVALID_CARD_NUMBER);
        bankAxeptCardNumberCheck()
        updateSaveButton()
    }
}

const expiryMonthOnChangeHandler = (e) => {
    State.printState()
    const value = e.target.value;
    if (!value || isNaN(value)) {
        updateFeedbackField(expiryDateFeedback, EXPIRY_MONTH_REQUIRED)
        setMonthExpiryError(true)
    } else {
        State.expiryMonth = value
        updateCardBannerComponents()
        setMonthExpiryError(false)
    }
    updateSaveButton()
    State.printState()
}

const expiryYearOnChangeHandler = (e) => {
    const value = e.target.value;
    if (!value || isNaN(value)) {
        updateFeedbackField(expiryDateFeedback, EXPIRY_YEAR_REQUIRED)
        setExpiryYearError(true)
    } else {
        State.expiryYear = value
        updateCardBannerComponents()
        setExpiryYearError(false)
    }
    expiryCardCheck()
    updateSaveButton()
}

const accountNumberOnKeyupHandler = (e) => {
    removeNonNumbersFromAccountNoField(e)
    updateCardBannerComponents()
    if (e.target.value.length === 11) {
        accountNumberCheck()
        updateFeedbackField(accountNumberFeedback, INVALID_CARD_NUMBER);
        updateSaveButton()
    }
}

const accountNumberOnChangeHandler = (e) => {
    removeNonNumbersFromAccountNoField(e)
    updateCardBannerComponents()
    accountNumberCheck()
    updateFeedbackField(accountNumberFeedback, INVALID_CARD_NUMBER);
    updateSaveButton()
}


//Event handler bindings
cardNumberField.onblur = e => cardNumberOnChangeHandler(e)
cardNumberField.onkeyup = e => cardNumberOnKeyupHandler(e)
expiryMonthField.onchange = e => expiryMonthOnChangeHandler(e)
expiryYearField.onchange = e => expiryYearOnChangeHandler(e)
accountNumberField.onkeyup = e => accountNumberOnKeyupHandler(e)
accountNumberField.onblur = e => accountNumberOnChangeHandler(e)


//initial setup
cardNumberField.setAttribute("maxLength", cardNumberMaxLengthByScheme(State.cardNumber, State.cardScheme).toString())

// const BinRangeForNorwegianDebitCard = {
//     Id: number,
//     BINRangeFrom: string,
//     BINRangeTo: string,
//     Brand: string,
//     Created: Date,
//     LastUpdated: Date | null
// }