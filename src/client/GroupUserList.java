package client;

import java.util.HashSet;
import java.util.Set;

public class GroupUserList {
	Set<String> users;

	public GroupUserList(Set<String> users) {
		this.users = users;
	}

	public GroupUserList(String user) {
		this.users = new HashSet<String>();
		this.users.add(user);
	}
	public GroupUserList() {
		this.users = new HashSet<String>();
	}

	public boolean addUser(String user) {
		if (!this.users.contains(user)) {
			this.users.add(user);
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupUserList other = (GroupUserList) obj;
		if (users == null) {
			if (other.users != null)
				return false;
		}
		GroupUserList otherUsers = (GroupUserList) obj;
		for (String i : this.users) {
			if (!otherUsers.getUsers().contains(i))
				return false;
		}
		for (String i : otherUsers.getUsers()) {
			if (!this.users.contains(i))
				return false;
		}
		return true;
	}
	public Set<String> getUsers(){
		return this.users;
	}
	@Override
	public String toString() {
		String returnStr="";
		for (String i : this.users) {
			returnStr+=i+" ";
	}
		return returnStr;
	}
}
