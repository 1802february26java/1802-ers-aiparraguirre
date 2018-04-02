window.onload = () =>{
 
    document.getElementById('username').innerHTML=sessionStorage.getItem('employeeUsername');
   
    document.getElementById("reimbursementButton").addEventListener("click", ()=>{
        
        let amount = document.getElementById("amount").value;
        
        if(amount <0){
            document.getElementById("reimbursementMessage").innerHTML = '<span class="label label-danger label-center">WARNING NEGATIVE AMOUNT.</span>';
            setTimeout(() =>{ document.getElementById("reimbursementMessage").innerHTML = '';}, 3000);
            return;
        }

        let description = document.getElementById("description").value;
       
        let reimbursementTypeName = document.getElementById("reimbursementTypeName").value;
        
        let reimbursementType;
        
        if(reimbursementTypeName==='COURSE'){
            reimbursementType = 2;
        }
        else if(reimbursementTypeName==='CERTIFICATION'){
            reimbursementType = 3;
        }
        else if(reimbursementTypeName==='TRAVELING'){
            reimbursementType = 4;
        }
        else{
            reimbursementType = 1;
        }
              
        let formdata = new FormData();
        formdata.append('amount',amount);
        formdata.append('description',description);
        formdata.append('reimbursementTypeName',reimbursementTypeName);
        formdata.append('reimbursementType',reimbursementType);
             
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
              
                let data = JSON.parse(xhr.responseText);

                createReimbursement(data);
            }
        };
       
        xhr.open("POST",`submitRequest.do?amount=${amount}&description=${description}
                            &reimbursementType=${reimbursementType}&reimbursementTypeName=${reimbursementTypeName}`);
        
        xhr.send(formdata);
      
    })
}

function disableAllComponents(){
    
    document.getElementById("amount").setAttribute("disabled","disabled");
    document.getElementById("description").setAttribute("disabled","disabled");

}

function createReimbursement(data) {
    
     disableAllComponents();

        console.log(data.message)
     
       if(data.message === "SUBMISSSION SUCCESSFUL!"){

        document.getElementById("reimbursementMessage").innerHTML = '<span class="label label-success label-center">CREATED SUCCESSFULLY.</span>';
        
        setTimeout(() =>{ window.location.replace("home.do");}, 400);
       
      }
      else{
        document.getElementById("reimbursementMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
           
      }
}