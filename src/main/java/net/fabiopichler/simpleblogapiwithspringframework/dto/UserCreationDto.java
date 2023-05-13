/*-------------------------------------------------------------------------------

Copyright (c) 2023 Fábio Pichler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

-------------------------------------------------------------------------------*/

package net.fabiopichler.simpleblogapiwithspringframework.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreationDto {

    @NotBlank(message = "Name may not be blank")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long")
    private final String name;

    @NotBlank(message = "username may not be blank")
    @Size(min = 2, max = 20, message = "username must be between 2 and 20 characters long")
    private final String username;

    @NotBlank(message = "Email may not be blank")
    @Size(min = 2, max = 255, message = "Email must be between 2 and 255 characters long")
    private final String email;

    @NotBlank(message = "password may not be blank")
    @Size(min = 8, max = 255, message = "Name must be between 8 and 255 characters long")
    private final String password;

    @NotBlank(message = "passwordRepeat may not be blank")
    @Size(min = 8, max = 255, message = "Name must be between 8 and 255 characters long")
    private final String password_confirmation;

    public UserCreationDto(String name, String username, String email, String password, String password_confirmation) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirmation() {
        return password_confirmation;
    }
}
