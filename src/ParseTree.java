public class ParseTree {
    public String token = "";

    public String getNextToken()
    {
        return "";
    }

    public boolean parseGui()
    {
        if (token == "Window")
        {
            token = getNextToken();
            //Here we can assume the next token is a string given that we can pass the value to the token..... I could be wrong.
            token = getNextToken();
            if (token == "(")
            {
                token = getNextToken();
                if(token.matches("^\\d+$")) // If the pattern matches that of an integer
                {
                    token = getNextToken();
                    if (token == ",")
                    {
                        token = getNextToken();
                        if(token.matches("^\\d+$"))
                        {
                            token = getNextToken();
                            if (token == ")")
                            {
                                token = getNextToken();
                                if (parseLayout())
                                {
                                    token = getNextToken();
                                    if(parseWidgets())
                                    {
                                        token=getNextToken();
                                        if (token == "End.")
                                        {
                                            return true;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean parseLayout()
    {
        token = getNextToken();
        if (token == "Layout")
        {
           token = getNextToken();
           if (parseLayout_type())
           {
               if (token == ":")
               {
                   return true;
               }
           }
        }

        return false;
    }

    public boolean parseLayout_type()
    {
        token = getNextToken();
        if( token == "Flow")
        {
            return true;
        }
        else if(token == "Grid")
        {
            token = getNextToken();
            if (token == "(")
            {
                token = getNextToken();
                if(token.matches("^\\d+$")) // If the pattern matches that of an integer
                {
                    token = getNextToken();
                    if (token == ",")
                    {
                        token = getNextToken();
                        if(token.matches("^\\d+$"))
                        {
                            token = getNextToken();
                            if (token == ",")
                            {
                                token = getNextToken();
                                if(token.matches("^\\d+$"))
                                {
                                    token = getNextToken();
                                    if (token == ",")
                                    {
                                        token = getNextToken();
                                        if(token.matches("^\\d+$"))
                                        {
                                            token = getNextToken();
                                        }

                                    }
                                }

                            }
                            if (token == ")")
                            {
                               return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    public boolean parseWidgets()
    {
        //token = getNextToken();
        if (parseWidget())
        {
            token = getNextToken();
            if (parseWidgets())
            {
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean parseWidget()
    {
        token = getNextToken();
        if (token == "Button")
        {
            token = getNextToken();
            token = getNextToken();
            if(token == ";")
            {
                return true;
            }

        }
        else if (token == "Group")
        {
            token = getNextToken();
            if(parseRadio_buttons())
            {
                token = getNextToken();
                if(token == "End;")
                {
                    return true;
                }
            }

        }
        else if (token == "Label")
        {
            token = getNextToken();
            token = getNextToken();
            if(parseRadio_buttons())
            {
                token = getNextToken();
                if(token == ";")
                {
                    return true;
                }
            }

        }
        else if (token == "Panel")
        {
            token = getNextToken();
            if(parseLayout())
            {
                token = getNextToken();
                if(parseWidgets())
                {
                    token = getNextToken();
                    if(token == "End;")
                    {
                        return true;
                    }
                }
            }
        }
        else if (token == "Textfield")
        {
            token = getNextToken();
            if(token.matches("^\\d+$"))
            {
                token = getNextToken();
                if(token == ";")
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean parseRadio_buttons()
    {
        if (parseRadio_button())
        {
            token = getNextToken();
            if (parseRadio_buttons())
            {
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean parseRadio_button()
    {
        if(token == "Radio")
        {
            token = getNextToken();
            if(token == ";")
            {
                return true;
            }
        }
        return false;
    }

}
