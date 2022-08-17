//Constants
const DEFAULT_CARD_SCHEME = 'unknown';

//helpers

//state
let state = {
    cardNumber: '',
    cardScheme: DEFAULT_CARD_SCHEME,
    accountNumber: '',
    isNorwegianCard : false,
    cardSubType: ''
}

//accessor function

//DOM element references

//card Banner
const cardAccountNoLabel = document.getElementById("cardAccountNoLabel")
const cardAccountNumber = document.getElementById("cardAccountNumber")
const bankAxeptLogo = document.getElementById("bankAxeptLogo")
const maskedCardNumberField = document.getElementById("maskedCardNumberField")
const cardBannerMonth = document.getElementById("cardBannerExpiryMonth")
const cardBannerExpiryYear = document.getElementById("cardBannerExpiryYear")
const cardBannerSchemeLogo = document.getElementById("cardBannerSchemeLogo")
const bankCard = document.getElementById("bankCard")

// card number
const cardNumberField = document.getElementById("cardNumberField")
const cardNumberSchemeLogo = document.getElementById("cardNumberSchemeLogo")
const cardNumberFeedback = document.getElementById("cardNumberFeedback")

// card expiry date
const expiryMonthField = document.getElementById("expiryMonthField")
const expiryYearField = document.getElementById("expiryYearField")
const expiryDateFeedback = document.getElementById("expiryDateFeedback")

// Account number
const accountNumberFieldContainer = document.getElementById("accountNumberFieldContainer")
const accountNumberFeedback = document.getElementById("accountNumberFeedback")

// Save Button
const saveButton = document.getElementById("saveButton")

//DOM update functions
const updateCardBannerElements = (cardNumber, cardScheme, isNorwegianCard) => {
    maskedCardNumberField.textContent = displayDigits(cardNumber, cardScheme)
    cardBannerSchemeLogo.classList.add(cardScheme)
    bankCard.classList.add(cardScheme)
    cardNumberSchemeLogo.classList.add(cardScheme)
    if (isNorwegianCard){
        bankAxeptLogo.classList.toggle("hidden", isNorwegianCard)
        cardAccountNoLabel.classList.toggle("hidden", isNorwegianCard)
        accountNumberFieldContainer.classList.toggle("hidden")
    }
}

const removeCardBannerElements = (cardNumber, cardScheme, isNorwegianCard) => {
    maskedCardNumberField.textContent = displayDigits(cardNumber, cardScheme)
    cardBannerSchemeLogo.classList.remove(cardScheme)
    bankCard.classList.remove(cardScheme)
    cardNumberSchemeLogo.classList.remove(cardScheme)
    if (isNorwegianCard){
        accountNumberFieldContainer.style.display = "none"
        bankAxeptLogo.style.display = "none"
        cardAccountNoLabel.style.display = "none"
    }
}

const removeFeedbackField = (feedbackField) => {
    feedbackField.textContent = ""
    feedbackField.style.display = "none"
};

const updateFeedbackField = (feedbackField) => {
    feedbackField.textContent = "Card number is invalid"
    feedbackField.style.display = "block"
};


//Event handlers
const displayDebitCardDigits = (accountNumber) => {
    if (accountNumber !== '' && accountNumber !== undefined) {
        return showDebitCardDigit(accountNumber ?? '')
    }

    return '.... .. .....'
}

const getCreditCardScheme = (value) => {
    if (!value || value.length < 2) {
        return 'unselected'
    }

    const valueStripped = value.replace(/\D/g, '') // Stripp everything but numbers  // old code (/[^0-9. ]\s/g, "");

    if (valueStripped.match(/^4[0-9]+$/)) {
        return 'visa'
    }
    if (valueStripped.match(/^5[1-5][0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^222[1-9][0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^22[3-9][0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^2[3-6][0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^27[01][0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^2720[0-9]+$/)) {
        return 'master'
    }
    if (valueStripped.match(/^(?:50[0-9]|5[6-8]|6[0-4]|67[0-9]|69[0-9])\d+$/)) {
        state.cardSubType = 'maestro'
        return'master'
    }
    if (valueStripped.match(/^3[47][0-9]+$/)) {
        return 'amex'
    }
    if (valueStripped.match(/^3(?:0[0-5]|09|[68][0-9])[0-9]+$/)) {
        return 'diners'
    }

    return DEFAULT_CARD_SCHEME
}

const removeNonNumbers =(e) => {
    e.target.value = e.target.value.replace(/\D/g, '')
    if (e.target.value === state.cardNumber) {
        return state.cardNumber
    }
    
    state.cardNumber = e.target.value
    return e.target.value
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

    if (cardScheme === 'visa') {
        checkCardSchemeLength = cardNumber.length === 16;
    }
    if (cardScheme === 'master') {
        checkCardSchemeLength = cardNumber.length === 16;
        if (state.cardSubType === 'maestro') {
            checkCardSchemeLength = cardNumber.length >= 12 && cardNumber.length <= 19;
        }
    }

    if (cardScheme === 'amex') {
        checkCardSchemeLength = cardNumber.length === 15;
    }
    if (cardScheme === 'diners') {
        checkCardSchemeLength = cardNumber.length >= 14 && cardNumber.length <= 16;
    }
    state.cardSubType = ''
    return checkCardSchemeLength
}

const isValidCardNumber =(cardNumber, cardScheme) => {
    if (!cardNumber) {
        return false
    }
    return luhnsAlgoCheck(cardNumber) && firstDigitCheck(cardNumber) && cardSchemeLengthOk(cardNumber, cardScheme)
}

const getCreditCardMaxLength = (cardNumber, cardScheme) => {
    if (cardScheme === 'visa') {
        return 16
    }
    if (cardScheme === 'master') {
        const valueStripped = cardNumber.replace(/\D/g, '') // Stripp everything but numbers
        const isMaestroCard = valueStripped.match(
            /^(?:50[0-9]|5[6-8]|6[0-4]|67[0-9]|69[0-9])\d+$/
        )
        if (isMaestroCard) {
            return 19
        }
        return 16
    }
    if (cardScheme === 'amex') {
        return 15
    }
    if (cardScheme === 'diners') {
        return 16
    }
    return 19
}

const validateCardNumber = (e) => {
    const cardNumber = removeNonNumbers(e)
    const cardScheme = getCreditCardScheme(cardNumber)

    state.cardScheme = cardScheme

    if (isValidCardNumber(cardNumber, cardScheme)){
        //todo check bankaxept card use bin rages and update isNorwegianCard state
        updateCardBannerElements(cardNumber, cardScheme, state.isNorwegianCard)
        removeFeedbackField(cardNumberFeedback, "", cardScheme, state.isNorwegianCard);
    }else {
        state.cardScheme = DEFAULT_CARD_SCHEME
        updateFeedbackField(cardNumberFeedback);
        removeCardBannerElements("", cardScheme, state.isNorwegianCard)
    }
}

const showLastFourDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')
    const addSpace = result.match(/.{1,4}/g)
    return addSpace?.join(' ')
}

const showDinersFourWithFourteenDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 10)
    const b3 = result.substring(10, 14)
    return `${b1} ${b2} ${b3}`
}

const showAmexFourDigit = value => {
    const result = value.replace(/.(?=.{4})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 10)
    const b3 = result.substring(10, 15)
    return `${b1} ${b2} ${b3}`
}

const showDebitCardDigit = value => {
    const result = value.replace(/.(?=.{5})/g, '*')

    const b1 = result.substring(0, 4)
    const b2 = result.substring(4, 6)
    const b3 = result.substring(6, 11)
    return `${b1} ${b2} ${b3}`
}

const displayDigits = (cardNumber, cardScheme) => {
    if (cardNumber !== undefined && cardScheme === 'amex') {
        return showAmexFourDigit(cardNumber)
    }
    if (cardNumber !== undefined && cardScheme === 'diners') {
        if (cardNumber !== '' && cardNumber.length === 16) {
            return showLastFourDigit(cardNumber)
        }
        if (cardNumber !== '' && cardNumber.length === 14) {
            return showDinersFourWithFourteenDigit(cardNumber)
        }
    }
    if (cardNumber !== undefined && cardScheme === 'visa') {
        return showLastFourDigit(cardNumber)
    }
    if (cardNumber !== undefined && cardScheme === 'master') {
        return showLastFourDigit(cardNumber)
    }

    if (cardNumber === '' || cardNumber === undefined) {
        return '.... .... .... ....'
    }

    return showLastFourDigit(cardNumber)
}


//Event handler bindings
cardNumberField.onblur = e => validateCardNumber(e)



//initial setup

cardNumberField.setAttribute("maxLength",getCreditCardMaxLength(state.cardNumber, state.cardScheme).toString())