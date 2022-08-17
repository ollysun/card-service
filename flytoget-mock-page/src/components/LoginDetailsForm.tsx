import Form from 'react-bootstrap/Form';
import EmailField from "./EmailField";
import PasswordField from "./PasswordField";
import MobilePhone from "./MobilePhone";

function LoginDetailsForm() {
  return (
    <Form>
        <EmailField/>
        <PasswordField/>
        <MobilePhone />
    </Form>
  );
}

export default LoginDetailsForm;