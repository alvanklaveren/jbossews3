/**
 *	Additional Javascript to for instance support click-event opening dropdownmenus in the leftsidemenu 
 */

/** 
 * Loop through all dropdown buttons to toggle between hiding and showing its dropdown content 
 * This allows the user to have multiple dropdowns without any conflict 
 */
var dropdown;
var i;

dropdown = document.getElementsByClassName("dropdown-btn");
for (i = 0; i < dropdown.length; i++) {
  dropdown[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var dropdownContent = this.nextElementSibling;
    if (dropdownContent.style.display === "block") {
      dropdownContent.style.display = "none";
    } else {
      dropdownContent.style.display = "block";
    }
  });
}