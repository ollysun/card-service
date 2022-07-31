import Container from "react-bootstrap/Container";
import LoginDetailsForm from "./LoginDetailsForm";

function LoginDetails() {
    return (
        <Container className="px-3">
            <p className="Form-title">Login Details</p>
            <LoginDetailsForm />
        </Container>
    );
}
export default LoginDetails;