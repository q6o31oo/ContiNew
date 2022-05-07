import { useSelector } from "react-redux";
import { RootState } from "src/store";
import styled from "styled-components";

interface ChatProps {
	chat: {
		room_id: string;
		sender: string;
		content: string;
		read_at?: string;
		created_at?: string;
		type?: string;
	};
}

interface ContainerProps {
	user: string;
	sender: string;
}

function ChatListitem({ chat }: ChatProps) {
	const { login_id } = useSelector((state: RootState) => state.userInfo);

	return (
		<Container user={login_id} sender={chat.sender}>
			<Textarea name="content" cols={20} readOnly defaultValue={chat.content} />
		</Container>
	);
}

const Container = styled.li<ContainerProps>`
	margin: 1rem;
	display: flex;
	justify-content: ${({ user, sender }) => (user === sender ? "end" : "start")};
`;

const Textarea = styled.textarea`
	font-size: 2rem;
	border: solid 1px #d3d3d3;
	resize: none;
	border-radius: 10px;
`;

export default ChatListitem;
