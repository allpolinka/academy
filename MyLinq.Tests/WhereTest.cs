namespace MyLinq.Tests
{
    public class WhereTest
    {
        [Fact]
        public void NotEmptyCollection_GetElementsMoreThanFive_Success()
        {
            //Arrange
            var array = new[] { 1, 2, 3, 4, 5, 10, 20 };
            var expected = new[] {10, 20};

            //Act
            var result = array.Where(o => o > 5);

            //Assert
            Assert.Equal(expected,result);

        }

        [Fact]
        public void NullSource_Throws()
        {
            //Arrange
            int[] array = null;
            
            //Act + Assert
            Assert.Throws<InvalidOperationException>(() => array.Where(o => o > 0)); //выложить тестовый двидок на гитхаб
        }

        [Fact]
        public void NotEmptyCollection_GetNonFitElements_EmptyResult()
        {
            var array = new[] { 1, 2, 3, 4, 5, 10, 20 };
            var result = array.Where(o => o > 5);
            Assert.Empty(result);
        }

        [Fact]
        public void CollectionWithtupels_GetSpecificOne_Success()
        {
            //Arange
            var array = new List<(int Garde, double Salary)> // кортеж
            {
                (Grade: 5, Salary: 10),
                (Grade: 10, Salary: 2400),
            };

            //Act
            var result = array.Where(o => o.Garde == 10);

            //Assert
            Assert.Single(result);
        }

        [Fact]
        public void CollectionWithElements_First_Success()
        {
            var item = new[] { 1, 2, 3, 4 };

            Assert.Equal(1, item.First());
        }

        [Fact]
        public void EmptyCollection_First_Throws()
        {
            var item = Array.Empty<int>();

            Assert.Throws<InvalidOperationException>(() => item.First());
        }

        [Fact]
        public void EmptyCollection_FirstOrDefault_ReturnsDefault()
        {
            var item = Array.Empty<int>();

            var result = item.FirstOrDefault();

            Assert.Equal(0, result);
        }

        [Fact]
        public void EmptyCollection_FirstOrDefault_ReturnsFirst()
        {
            var item = new[] { 1, 2, 3 };

            var result = item.FirstOrDefault();

            Assert.Equal(1, result);
        }

        [Fact]
        public void NonEmptyCollection_FirstOrDefaultWithCriteria_ReturnsMatching()
        {
            var item = new[] { 1, 2, 3 };

            var result = item.FirstOrDefault(item => item == 3);

            Assert.Equal(3, result);
        }

        [Fact]
        public void NonEmptyCollection_FirstOrDefaultWithCriteria_NoMatchingElement_ReturnsDefault()
        {
            var item = new[] { 1, 2, 3 };

            var result = item.FirstOrDefault(item => item == 33);

            Assert.Equal(0, result);
        }
    }
}