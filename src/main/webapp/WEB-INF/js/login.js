window.onload=()=>{
    /**
     * Redirect user to the right html if they come from somewhere else
     */
    if(window.location.pathname !=='/FrontController/login.do'){
        window.location.replace(login.do);
    }
     //login Event Listener
    document.getElementById("login").addEventListener("click",()=>{
        //calback function
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;
        //Ajax logic

        let xhr = new XMLHttpRequest();//object instance creation

        xhr.onreadystatechange = ()=>{
            // instead of 4 you can also do xmlhttprequest.done
            if(xhr.readyState === 4 && xhr.status === 200){
                // Getting JSON from response body
                let data = JSON.parse(xhr.responseText);
                
                console.log(data);
                // call login response processing

                login(data);
            }

        };
        //doing a HTTP to a specific endpoint
        xhr.open("POST", `login.do?username=${username}&password=${password}`);

        //Sending our request
        xhr.send();
    });

}

function login(data){
    //falsey truthy
    // if message is a memever of JSON then it is authentication failed
    if(data.message){
        document.getElementById('loginMessage').innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
    }
    else{
        // fifth way to manage a small session sessionStorage of JavaScript
        sessionStorage.setItem("customerId", data.id);
        sessionStorage.setItem("customerUsername",data.username);

        //redirect in javascript
        window.location.replace("home.do");
     }
}