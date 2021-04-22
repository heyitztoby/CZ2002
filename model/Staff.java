package app.model;

import java.io.Serializable;

/**
 * Staff class which consists of all the mutators and accessors methods
 * @author Jian Wei
 */
public class Staff implements Serializable{
	String id;
	char gender;
	String name;
	String jobTitle;
	/**
	 *Constructor initializing id, gender, name and job title according to parameters
	 *@param id
	 *@param gender
	 *@param name
	 *@param jobTitle
	 */
	public Staff(String id, char gender, String name, String jobTitle) {
		this.id = id;
		this.gender = gender;
		this.name = name;
		this.jobTitle = jobTitle;
	}
	/**
	 *Return Id of type string
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 *Setting Id of type string
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 *Return gender of type char
	 * @return
	 */
	public char getGender() {
		return gender;
	}
	/**
	 *Setting gender of type char
	 * @param gender
	 */
	public void setGender(char gender) {
		this.gender = gender;
	}
	/**
	 *Return name of type string
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 *Setting name of type string
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 *Return jobTitle of type string
	 * @return
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 *Setting jobTitle of type string
	 * @param jobTitle
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	
    /**
     * Returns a hash code value for the object.
     * @return  a hash code value for this object.
     * @see     java.lang.Object#hashCode()
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gender;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((jobTitle == null) ? 0 : jobTitle.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

    /**
     * Indicates whether some other object is "equal to" this one.
     * @return  {@code true} if this object is the same as the obj
     * @see     java.lang.Object#equals(Object)
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Staff other = (Staff) obj;
		if (gender != other.gender)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (jobTitle == null) {
			if (other.jobTitle != null)
				return false;
		} else if (!jobTitle.equals(other.jobTitle))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Staff [id=" + id + ", gender=" + gender + ", name=" + name + ", jobTitle=" + jobTitle + "]";
	}

	
	
}
