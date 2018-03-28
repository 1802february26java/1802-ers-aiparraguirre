window.onload=()=>{
    /**
     * Redirect user to the right html if they come from somewhere else
     */
    if(window.location.pathname !=='/ERS/login.do'){
       window.location.replace(login.do);
   }
     //login Event Listener
    document.getElementById("login").addEventListener("click",()=>{
        //callback function
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;
        //Ajax logic
       // alert(document.getElementById("username").value);
        let xhr = new XMLHttpRequest();//object instance creation

        xhr.onreadystatechange = ()=>{
            
            if(xhr.readyState === 4 && xhr.status === 200){
                //console.log(xhr.readyState);
                //console.log(xhr.status);
                //console.log(xhr.responseText);
                let data = JSON.parse(xhr.responseText);
                //console.log(data);
                login(data);
            }

        };

        //doing a HTTP to a specific endpoint

        //console.log('before the post');

        xhr.open('POST', `login.do?username=${username}&password=${password}`);

       // console.log('WE are past xhr post');
        //Sending our request

        xhr.send();
    });

}

function login(data){
    //falsey truthy
    // if message is a memever of JSON then it is authentication failed
    console.log('Inside of the logindata');

   if(data.message){

    console.log('Inside the IF of the logindata');
        document.getElementById('loginMessage').innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
    }
    else{

        console.log('Inside the else of the logindata');
        // fifth way to manage a small session sessionStorage of JavaScript
        sessionStorage.setItem("employeeId", data.id);
        sessionStorage.setItem("employeeUsername",data.username);
        sessionStorage.setItem("employeeFirstName", data.firstName);
        sessionStorage.setItem("employeeLastName", data.lastName);
        sessionStorage.setItem("employeeEmail", data.email);
        sessionStorage.setItem("employeeRole", data.employeeRole.id);
        //redirect in javascript
        if(data.employeeRole === 2){
            window.location.replace("homeManager.do");  
        }
            window.location.replace("homeEmployee.do");
     
        // DO 
    }
}