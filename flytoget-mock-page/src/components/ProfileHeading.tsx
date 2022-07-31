import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

function ProfileHeading() {
    return (
        <Container>
            <Row className="justify-content-center my-4">
               <h1 className="Page-heading">Register Profile</h1>
            </Row>
            <Row className="justify-content-center my-4">
                <h2 className="Sub-heading">Create your own profile for My Page.</h2>
            </Row>
        </Container>
    );
}
export default ProfileHeading;