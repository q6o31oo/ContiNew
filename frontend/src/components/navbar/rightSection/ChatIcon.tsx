import { faRocketchat } from "@fortawesome/free-brands-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styled from "styled-components";

function ChatIcon() {
	return (
		<>
			<FontAwesome icon={faRocketchat} />
		</>
	);
}

const FontAwesome = styled(FontAwesomeIcon)`
	width: 2rem;
	height: 2rem;
	margin-right: 1.5rem;
`;

export default ChatIcon;