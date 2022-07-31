import Container from "react-bootstrap/Container";

function Iframe() {
    return (
        <Container className="px-3">
            <iframe className="App-iframe" title="card registration iframe"
                    src={process.env.REACT_APP_IFRAME_URL}/>
        </Container>
    );
}
export default Iframe;