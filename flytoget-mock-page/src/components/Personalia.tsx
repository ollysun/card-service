import Container from "react-bootstrap/Container";
import FirstName from "./FirstName";
import LastName from "./LastName";
import DateOfBirth from "./DateOfBirth";
import TicketTypeField from "./TicketTypeField";

function Personalia() {
    return (
        <Container className="px-3">
            <p className="Form-title">Personalia</p>
           <FirstName />
            <LastName />
            <DateOfBirth />
            <TicketTypeField />
        </Container>
    );
}
export default Personalia;