window.onload=()=>{
    
    document.getElementById("login").addEventListener("click",()=>{
        
            let username = document.getElementById("username").value;
            let password = document.getElementById("password").value;
        
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = ()=>{
            
            if(xhr.readyState === 4 && xhr.status === 200){
                
                let data = JSON.parse(xhr.responseText);
                
                login(data);
            }

        };

        xhr.open('POST', `login.do?username=${username}&password=${password}`);

        xhr.send();
    });

}

function login(data){
      console.log('Inside of the logindata');

   if(data.message){

    console.log('Inside the IF of the logindata');
        document.getElementById('loginMessage').innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
    }
    else{

        console.log('Inside the else of the logindata');
       
        sessionStorage.setItem("employeeId", data.id);
        sessionStorage.setItem("employeeUsername",data.username);
        sessionStorage.setItem("employeeFirstName", data.firstName);
        sessionStorage.setItem("employeeLastName", data.lastName);
        sessionStorage.setItem("employeeEmail", data.email);
        sessionStorage.setItem("employeeRole", data.employeeRole.id);

        window.location.replace("home.do");  
        
       
         
    }
}