import Container from "react-bootstrap/Container";
import FirstName from "./FirstName";
import LastName from "./LastName";
import DateOfBirth from "./DateOfBirth";
import TicketType from "./TicketType";

function Personalia() {
    return (
        <Container className="px-3">
            <p className="Form-title">Personalia</p>
           <FirstName />
            <LastName />
            <DateOfBirth />
            <TicketType />
        </Container>
    );
}
export default Personalia;